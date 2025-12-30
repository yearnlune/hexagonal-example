package io.github.yearnlune.hexagonal.payment.application.query

data class GetRefundsByPaymentIdQuery(
    val paymentId: Long,
)
