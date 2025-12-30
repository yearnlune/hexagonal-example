package io.github.yearnlune.hexagonal.payment.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.yearnlune.hexagonal.global.exception.BusinessException
import io.github.yearnlune.hexagonal.global.exception.ErrorCode
import io.github.yearnlune.hexagonal.payment.application.command.ApprovePaymentCommand
import io.github.yearnlune.hexagonal.payment.application.command.CreatePaymentCommand
import io.github.yearnlune.hexagonal.payment.application.query.GetPaymentByOrderIdQuery
import io.github.yearnlune.hexagonal.payment.application.query.GetPaymentQuery
import io.github.yearnlune.hexagonal.payment.application.result.PaymentResult
import io.github.yearnlune.hexagonal.payment.domain.payment.Payment
import io.github.yearnlune.hexagonal.payment.domain.payment.PaymentHistory
import io.github.yearnlune.hexagonal.payment.domain.payment.vo.PaymentHistoryType
import io.github.yearnlune.hexagonal.payment.infrastructure.PaymentHistoryRepository
import io.github.yearnlune.hexagonal.payment.infrastructure.PaymentRepository
import io.github.yearnlune.hexagonal.payment.infrastructure.client.TossPaymentClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val paymentHistoryRepository: PaymentHistoryRepository,
    private val tossPaymentClient: TossPaymentClient,
    private val objectMapper: ObjectMapper,
) {
    @Transactional
    fun createPayment(command: CreatePaymentCommand): PaymentResult {
        val payment =
            Payment.create(
                orderId = command.orderId,
                amount = command.amount,
                paymentKey = command.paymentKey,
                orderName = command.orderName,
            )

        val savedPayment = paymentRepository.save(payment)

        val history =
            PaymentHistory.create(
                payment = savedPayment,
                type = PaymentHistoryType.REQUEST,
                message = "결제 요청 생성",
                requestData = objectMapper.writeValueAsString(command),
            )
        paymentHistoryRepository.save(history)

        return PaymentResult.from(savedPayment)
    }

    @Transactional
    fun approvePayment(command: ApprovePaymentCommand): PaymentResult {
        val orderIdLong = command.orderId.toLong()
        val payment =
            paymentRepository.findByOrderId(orderIdLong)
                ?: throw BusinessException(
                    ErrorCode.PAYMENT_NOT_FOUND,
                    params = mapOf("orderId" to orderIdLong),
                )

        val tossRequest =
            TossPaymentClient.TossPaymentRequest(
                paymentKey = command.paymentKey,
                orderId = command.orderId,
                amount = command.amount,
            )

        val tossResponse =
            try {
                tossPaymentClient.approvePayment(tossRequest)
            } catch (e: Exception) {
                payment.fail()
                val savedPayment = paymentRepository.save(payment)

                val history =
                    PaymentHistory.create(
                        payment = savedPayment,
                        type = PaymentHistoryType.FAILED,
                        message = "결제 승인 실패: ${e.message}",
                        requestData = objectMapper.writeValueAsString(tossRequest),
                        responseData = e.message,
                    )
                paymentHistoryRepository.save(history)

                throw BusinessException(
                    ErrorCode.PAYMENT_APPROVE_FAILED,
                    params = mapOf("message" to (e.message ?: "결제 승인 실패")),
                )
            }

        if (tossResponse.status == "DONE") {
            payment.approve()
        } else {
            payment.fail()
        }

        val savedPayment = paymentRepository.save(payment)

        val history =
            PaymentHistory.create(
                payment = savedPayment,
                type = if (tossResponse.status == "DONE") PaymentHistoryType.APPROVED else PaymentHistoryType.FAILED,
                message = if (tossResponse.status == "DONE") "결제 승인 완료" else "결제 승인 실패",
                requestData = objectMapper.writeValueAsString(tossRequest),
                responseData = objectMapper.writeValueAsString(tossResponse),
            )
        paymentHistoryRepository.save(history)

        return PaymentResult.from(savedPayment)
    }

    fun getPayment(query: GetPaymentQuery): PaymentResult {
        val payment =
            paymentRepository.findByIdOrNull(query.paymentId)
                ?: throw BusinessException(
                    ErrorCode.PAYMENT_NOT_FOUND,
                    params = mapOf("paymentId" to query.paymentId),
                )

        return PaymentResult.from(payment)
    }

    fun getPaymentByOrderId(query: GetPaymentByOrderIdQuery): PaymentResult {
        val payment =
            paymentRepository.findByOrderId(query.orderId)
                ?: throw BusinessException(
                    ErrorCode.PAYMENT_NOT_FOUND,
                    params = mapOf("orderId" to query.orderId),
                )

        return PaymentResult.from(payment)
    }
}
