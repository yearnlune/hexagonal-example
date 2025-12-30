package io.github.yearnlune.hexagonal.payment.application.result

import io.github.yearnlune.hexagonal.payment.domain.payment.Payment
import io.github.yearnlune.hexagonal.payment.domain.payment.vo.PaymentStatus
import java.time.LocalDateTime

data class PaymentResult(
    val id: Long,
    val orderId: Long,
    val amount: Int,
    val paymentKey: String,
    val orderName: String,
    val status: PaymentStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(payment: Payment) =
            PaymentResult(
                id = payment.id,
                orderId = payment.orderId,
                amount = payment.amount,
                paymentKey = payment.paymentKey,
                orderName = payment.orderName,
                status = payment.status,
                createdAt = payment.createdAt,
                updatedAt = payment.updatedAt,
            )
    }
}
