package io.github.yearnlune.hexagonal.order.application.command

data class OrderItemCommand(
    val productId: Long,
    val quantity: Int,
)
