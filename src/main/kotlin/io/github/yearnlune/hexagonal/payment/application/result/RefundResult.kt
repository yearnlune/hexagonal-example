package io.github.yearnlune.hexagonal.payment.application.result

import io.github.yearnlune.hexagonal.payment.domain.refund.Refund
import io.github.yearnlune.hexagonal.payment.domain.refund.vo.RefundStatus
import java.time.LocalDateTime

data class RefundResult(
    val id: Long,
    val paymentId: Long,
    val amount: Int,
    val reason: String,
    val cancelKey: String,
    val idempotencyKey: String,
    val status: RefundStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(refund: Refund) =
            RefundResult(
                id = refund.id,
                paymentId = refund.paymentId,
                amount = refund.amount,
                reason = refund.reason,
                cancelKey = refund.cancelKey,
                idempotencyKey = refund.idempotencyKey,
                status = refund.status,
                createdAt = refund.createdAt,
                updatedAt = refund.updatedAt,
            )
    }
}
