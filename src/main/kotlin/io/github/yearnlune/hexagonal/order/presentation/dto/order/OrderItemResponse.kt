package io.github.yearnlune.hexagonal.order.presentation.dto.order

data class OrderItemResponse(
    val productId: Long,
    val productName: String,
    val price: Int,
    val quantity: Int,
)
