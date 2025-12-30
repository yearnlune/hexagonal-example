package io.github.yearnlune.hexagonal.order.application.result

import io.github.yearnlune.hexagonal.order.domain.order.Order

data class OrderResult(
    val id: Long,
    val userId: Long,
    val items: List<OrderItemResult>,
) {
    companion object {
        fun from(order: Order) =
            OrderResult(
                id = order.id,
                userId = order.userId,
                items = order.items.map { OrderItemResult.from(it) },
            )
    }
}
