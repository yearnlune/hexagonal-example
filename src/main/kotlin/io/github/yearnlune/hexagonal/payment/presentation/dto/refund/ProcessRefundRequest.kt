package io.github.yearnlune.hexagonal.payment.presentation.dto.refund

data class ProcessRefundRequest(
    val idempotencyKey: String,
)
