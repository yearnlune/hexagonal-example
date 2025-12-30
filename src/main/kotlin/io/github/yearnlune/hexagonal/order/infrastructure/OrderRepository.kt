package io.github.yearnlune.hexagonal.order.infrastructure

import io.github.yearnlune.hexagonal.order.domain.order.Order
import io.github.yearnlune.hexagonal.order.domain.order.vo.OrderStatus
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class OrderRepository(
    private val jpa: JpaOrderRepository,
) {
    fun save(order: Order): Order = jpa.save(order)

    fun findByIdOrNull(id: Long): Order? = jpa.findByIdOrNull(id)

    fun findAll(): List<Order> = jpa.findAll()

    fun findByUserId(userId: Long): List<Order> = jpa.findByUserId(userId)

    fun findByUserIdAndStatus(
        userId: Long,
        status: OrderStatus,
    ): List<Order> = jpa.findByUserIdAndStatus(userId, status)
}
