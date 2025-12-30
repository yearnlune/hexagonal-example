package io.github.yearnlune.hexagonal.payment.application.query

data class GetPaymentByOrderIdQuery(
    val orderId: Long,
)
