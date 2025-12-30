package io.github.yearnlune.hexagonal.payment.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.yearnlune.hexagonal.global.exception.BusinessException
import io.github.yearnlune.hexagonal.global.exception.ErrorCode
import io.github.yearnlune.hexagonal.order.infrastructure.OrderRepository
import io.github.yearnlune.hexagonal.payment.application.command.CreateRefundCommand
import io.github.yearnlune.hexagonal.payment.application.command.ProcessRefundCommand
import io.github.yearnlune.hexagonal.payment.application.query.GetRefundQuery
import io.github.yearnlune.hexagonal.payment.application.query.GetRefundsByPaymentIdQuery
import io.github.yearnlune.hexagonal.payment.application.result.RefundRequestKeyResult
import io.github.yearnlune.hexagonal.payment.application.result.RefundResult
import io.github.yearnlune.hexagonal.payment.domain.payment.PaymentHistory
import io.github.yearnlune.hexagonal.payment.domain.payment.vo.PaymentHistoryType
import io.github.yearnlune.hexagonal.payment.domain.payment.vo.PaymentStatus
import io.github.yearnlune.hexagonal.payment.domain.refund.Refund
import io.github.yearnlune.hexagonal.payment.domain.refund.vo.RefundStatus
import io.github.yearnlune.hexagonal.payment.infrastructure.PaymentHistoryRepository
import io.github.yearnlune.hexagonal.payment.infrastructure.PaymentRepository
import io.github.yearnlune.hexagonal.payment.infrastructure.RefundIdempotencyKeyRepository
import io.github.yearnlune.hexagonal.payment.infrastructure.RefundRepository
import io.github.yearnlune.hexagonal.payment.infrastructure.client.TossPaymentClient
import java.math.BigDecimal
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RefundUseCase(
    private val refundRepository: RefundRepository,
    private val paymentRepository: PaymentRepository,
    private val paymentHistoryRepository: PaymentHistoryRepository,
    private val tossPaymentClient: TossPaymentClient,
    private val objectMapper: ObjectMapper,
    private val refundIdempotencyKeyRepository: RefundIdempotencyKeyRepository,
    private val orderRepository: OrderRepository,
) {
    fun generateRefundRequestKey(command: CreateRefundCommand): RefundRequestKeyResult {
        val payment =
            paymentRepository.findByIdOrNull(command.paymentId)
                ?: throw BusinessException(
                    ErrorCode.PAYMENT_NOT_FOUND,
                    params = mapOf("paymentId" to command.paymentId),
                )

        if (payment.status != PaymentStatus.APPROVED) {
            throw BusinessException(
                ErrorCode.INVALID_ORDER_STATUS,
                params = mapOf("message" to "승인된 결제만 환불할 수 있습니다"),
            )
        }

        val idempotencyKeyComponent = UUID.randomUUID().toString()
        val idempotencyKey =
            refundIdempotencyKeyRepository.generateIdempotencyKey(
                idempotencyKeyComponent = idempotencyKeyComponent,
                paymentKey = payment.paymentKey,
                refundAmount = BigDecimal(command.amount),
                refundReason = command.reason,
                requestedBy = command.requestedBy,
            )

        refundIdempotencyKeyRepository.saveIdempotencyKey(
            paymentId = command.paymentId,
            amount = command.amount,
            reason = command.reason,
            cancelKey = command.cancelKey,
            idempotencyKey = idempotencyKey,
        )

        return RefundRequestKeyResult(idempotencyKey = idempotencyKey)
    }

    @Transactional
    fun processRefundWithKey(command: ProcessRefundCommand): RefundResult {
        val keyData =
            refundIdempotencyKeyRepository.getIdempotencyKeyData(command.idempotencyKey)
                ?: throw BusinessException(
                    ErrorCode.REFUND_NOT_FOUND,
                    params =
                        mapOf(
                            "idempotencyKey" to command.idempotencyKey,
                            "message" to "멱등성 키가 만료되었거나 존재하지 않습니다",
                        ),
                )

        val existingRefund =
            refundRepository.findByIdempotencyKey(command.idempotencyKey)
                ?: run {
                    val refund =
                        Refund.create(
                            paymentId = keyData.paymentId,
                            amount = keyData.amount,
                            reason = keyData.reason,
                            cancelKey = keyData.cancelKey,
                            idempotencyKey = command.idempotencyKey,
                        )
                    refundRepository.save(refund)
                }

        if (existingRefund.status != RefundStatus.PENDING) {
            return RefundResult.from(existingRefund)
        }

        return processRefund(existingRefund)
    }

    private fun processRefund(existingRefund: Refund): RefundResult {
        val payment =
            paymentRepository.findByIdOrNull(existingRefund.paymentId)
                ?: throw BusinessException(
                    ErrorCode.PAYMENT_NOT_FOUND,
                    params = mapOf("paymentId" to existingRefund.paymentId),
                )

        val tossRequest =
            TossPaymentClient.TossCancelRequest(
                cancelReason = existingRefund.reason,
            )

        val tossResponse =
            try {
                tossPaymentClient.cancelPayment(
                    existingRefund.cancelKey,
                    existingRefund.idempotencyKey,
                    tossRequest,
                )
            } catch (e: Exception) {
                existingRefund.fail()
                val failedRefund = refundRepository.save(existingRefund)

                val history =
                    PaymentHistory.create(
                        payment = payment,
                        type = PaymentHistoryType.FAILED,
                        message = "환불 실패: ${e.message}",
                        requestData = objectMapper.writeValueAsString(tossRequest),
                        responseData = e.message,
                    )
                paymentHistoryRepository.save(history)

                throw BusinessException(
                    ErrorCode.PAYMENT_CANCEL_FAILED,
                    params = mapOf("message" to (e.message ?: "환불 실패")),
                )
            }

        if (tossResponse.status == "CANCELED") {
            existingRefund.approve()
            payment.cancel()
            paymentRepository.save(payment)
        } else {
            existingRefund.fail()
        }

        val finalRefund = refundRepository.save(existingRefund)

        val history =
            PaymentHistory.create(
                payment = payment,
                type = PaymentHistoryType.CANCELLED,
                message = "환불 완료",
                requestData = objectMapper.writeValueAsString(tossRequest),
                responseData = objectMapper.writeValueAsString(tossResponse),
            )
        paymentHistoryRepository.save(history)

        return RefundResult.from(finalRefund)
    }

    fun getRefund(query: GetRefundQuery): RefundResult {
        val refund =
            refundRepository.findByIdOrNull(query.refundId)
                ?: throw BusinessException(
                    ErrorCode.PAYMENT_NOT_FOUND,
                    params = mapOf("refundId" to query.refundId),
                )

        return RefundResult.from(refund)
    }

    fun getRefundsByPaymentId(query: GetRefundsByPaymentIdQuery): List<RefundResult> =
        refundRepository
            .findByPaymentId(query.paymentId)
            .map { RefundResult.from(it) }
}
