package io.github.yearnlune.hexagonal.order.application.result

import io.github.yearnlune.hexagonal.order.domain.order.OrderItem

data class OrderItemResult(
    val productId: Long,
    val productName: String,
    val price: Int,
    val quantity: Int,
) {
    companion object {
        fun from(item: OrderItem) =
            OrderItemResult(
                productId = item.productId,
                productName = item.productName,
                price = item.price,
                quantity = item.quantity,
            )
    }
}
