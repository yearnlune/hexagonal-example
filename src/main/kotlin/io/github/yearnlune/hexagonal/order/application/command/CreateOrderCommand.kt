package io.github.yearnlune.hexagonal.order.application.command

data class CreateOrderCommand(
    val userId: Long,
    val items: List<OrderItemCommand>,
)
