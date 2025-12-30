package io.github.yearnlune.hexagonal.payment.application.command

data class ProcessRefundCommand(
    val idempotencyKey: String,
)
