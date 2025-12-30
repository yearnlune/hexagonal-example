package io.github.yearnlune.hexagonal.order.domain.order

import io.github.yearnlune.hexagonal.global.entity.BaseEntity
import io.github.yearnlune.hexagonal.global.exception.BusinessException
import io.github.yearnlune.hexagonal.global.exception.ErrorCode
import io.github.yearnlune.hexagonal.order.domain.order.vo.OrderStatus
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
class Order(
    @Column(nullable = false)
    val userId: Long,
    status: OrderStatus = OrderStatus.PENDING,
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val _items: MutableList<OrderItem> = mutableListOf(),
) : BaseEntity() {
    @Column(nullable = true)
    var paymentId: Long? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: OrderStatus = status
        protected set

    val items: List<OrderItem>
        get() = _items.toList()

    fun addItem(item: OrderItem) {
        _items.add(item)
        item.attach(this)
    }

    fun removeItem(item: OrderItem) {
        _items.remove(item)
    }

    fun calculateTotalAmount(): Int = _items.sumOf { it.calculateAmount() }

    fun confirm() {
        if (_items.isEmpty()) {
            throw BusinessException(ErrorCode.EMPTY_ORDER_ITEMS)
        }
        status = OrderStatus.CONFIRMED
    }

    fun cancel() {
        if (status == OrderStatus.DELIVERED) {
            throw BusinessException(
                ErrorCode.INVALID_ORDER_STATUS,
                params = mapOf("message" to "배송 완료된 주문은 취소할 수 없습니다"),
            )
        }
        status = OrderStatus.CANCELLED
    }

    fun startShipping() {
        if (status != OrderStatus.CONFIRMED) {
            throw BusinessException(
                ErrorCode.INVALID_ORDER_STATUS,
                params = mapOf("message" to "확정된 주문만 배송을 시작할 수 있습니다"),
            )
        }
        status = OrderStatus.SHIPPED
    }

    fun completeDelivery() {
        if (status != OrderStatus.SHIPPED) {
            throw BusinessException(
                ErrorCode.INVALID_ORDER_STATUS,
                params = mapOf("message" to "배송 중인 주문만 배송 완료 처리할 수 있습니다"),
            )
        }
        status = OrderStatus.DELIVERED
    }

    fun linkPayment(paymentId: Long) {
        this.paymentId = paymentId
    }

    companion object {
        fun create(
            userId: Long,
            items: List<OrderItem>,
        ): Order {
            val order = Order(userId = userId)
            items.forEach { order.addItem(it) }
            return order
        }
    }
}
