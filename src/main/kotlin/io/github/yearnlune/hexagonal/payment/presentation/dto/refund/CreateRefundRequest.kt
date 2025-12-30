package io.github.yearnlune.hexagonal.payment.presentation.dto.refund

data class CreateRefundRequest(
    val paymentId: Long,
    val amount: Int,
    val reason: String,
    val cancelKey: String,
    val requestedBy: Long,
)
