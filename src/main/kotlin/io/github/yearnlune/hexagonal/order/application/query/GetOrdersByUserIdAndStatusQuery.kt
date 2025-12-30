package io.github.yearnlune.hexagonal.order.application.query

import io.github.yearnlune.hexagonal.order.domain.order.vo.OrderStatus

data class GetOrdersByUserIdAndStatusQuery(
    val userId: Long,
    val status: OrderStatus,
)
