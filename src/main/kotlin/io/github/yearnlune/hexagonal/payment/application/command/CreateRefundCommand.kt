package io.github.yearnlune.hexagonal.payment.application.command

data class CreateRefundCommand(
    val paymentId: Long,
    val amount: Int,
    val reason: String,
    val cancelKey: String,
    val requestedBy: Long,
)
