package io.github.yearnlune.hexagonal.order.presentation.dto.order

data class CreateOrderRequest(
    val userId: Long,
    val items: List<OrderItemRequest>,
)
