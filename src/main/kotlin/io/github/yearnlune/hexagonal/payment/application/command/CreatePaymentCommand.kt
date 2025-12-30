package io.github.yearnlune.hexagonal.payment.application.command

data class CreatePaymentCommand(
    val orderId: Long,
    val amount: Int,
    val paymentKey: String,
    val orderName: String,
)
