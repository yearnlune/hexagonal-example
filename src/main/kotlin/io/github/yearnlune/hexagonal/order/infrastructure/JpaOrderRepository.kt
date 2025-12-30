package io.github.yearnlune.hexagonal.order.infrastructure

import io.github.yearnlune.hexagonal.order.domain.order.Order
import io.github.yearnlune.hexagonal.order.domain.order.vo.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository

interface JpaOrderRepository : JpaRepository<Order, Long> {
    fun findByUserId(userId: Long): List<Order>

    fun findByUserIdAndStatus(
        userId: Long,
        status: OrderStatus,
    ): List<Order>
}
