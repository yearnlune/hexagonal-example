package io.github.yearnlune.hexagonal.payment.presentation.dto.payment

data class ApprovePaymentRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: Int,
)
