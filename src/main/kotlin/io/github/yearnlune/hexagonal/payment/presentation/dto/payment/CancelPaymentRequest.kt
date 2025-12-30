package io.github.yearnlune.hexagonal.payment.presentation.dto.payment

data class CancelPaymentRequest(
    val paymentKey: String,
    val cancelReason: String,
)
