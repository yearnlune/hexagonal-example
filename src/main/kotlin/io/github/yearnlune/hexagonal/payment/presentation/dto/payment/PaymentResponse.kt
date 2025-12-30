package io.github.yearnlune.hexagonal.payment.presentation.dto.payment

import io.github.yearnlune.hexagonal.payment.domain.payment.vo.PaymentStatus
import java.time.LocalDateTime

data class PaymentResponse(
    val id: Long,
    val orderId: Long,
    val amount: Int,
    val paymentKey: String,
    val orderName: String,
    val status: PaymentStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
