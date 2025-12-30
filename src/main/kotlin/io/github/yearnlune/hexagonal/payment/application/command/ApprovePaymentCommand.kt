package io.github.yearnlune.hexagonal.payment.application.command

data class ApprovePaymentCommand(
    val paymentKey: String,
    val orderId: String,
    val amount: Int,
)
