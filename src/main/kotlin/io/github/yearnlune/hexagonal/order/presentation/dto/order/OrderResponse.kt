package io.github.yearnlune.hexagonal.order.presentation.dto.order

data class OrderResponse(
    val id: Long,
    val userId: Long,
    val items: List<OrderItemResponse>,
)
