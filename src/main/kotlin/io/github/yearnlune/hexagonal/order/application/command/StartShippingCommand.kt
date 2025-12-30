package io.github.yearnlune.hexagonal.order.application.command

data class StartShippingCommand(
    val orderId: Long,
)
