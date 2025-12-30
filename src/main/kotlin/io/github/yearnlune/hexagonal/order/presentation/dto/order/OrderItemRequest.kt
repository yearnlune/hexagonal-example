package io.github.yearnlune.hexagonal.order.presentation.dto.order

data class OrderItemRequest(
    val productId: Long,
    val quantity: Int,
)
