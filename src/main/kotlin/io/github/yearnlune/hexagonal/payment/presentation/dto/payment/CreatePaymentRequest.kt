package io.github.yearnlune.hexagonal.payment.presentation.dto.payment

data class CreatePaymentRequest(
    val orderId: Long,
    val amount: Int,
    val paymentKey: String,
    val orderName: String,
)
