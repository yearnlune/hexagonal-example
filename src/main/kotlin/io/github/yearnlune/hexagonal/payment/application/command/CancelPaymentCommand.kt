package io.github.yearnlune.hexagonal.payment.application.command

data class CancelPaymentCommand(
    val paymentKey: String,
    val cancelReason: String,
)
