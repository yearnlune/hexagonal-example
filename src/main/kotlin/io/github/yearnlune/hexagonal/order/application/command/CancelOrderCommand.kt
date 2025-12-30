package io.github.yearnlune.hexagonal.order.application.command

data class CancelOrderCommand(
    val orderId: Long,
)
