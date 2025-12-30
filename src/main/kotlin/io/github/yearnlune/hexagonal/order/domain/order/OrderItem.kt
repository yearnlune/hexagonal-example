package io.github.yearnlune.hexagonal.order.domain.order

import io.github.yearnlune.hexagonal.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "order_items")
class OrderItem(
    @Column(nullable = false)
    val productId: Long,
    @Column(nullable = false, length = 200)
    val productName: String,
    @Column(nullable = false)
    val price: Int,
    @Column(nullable = false)
    val quantity: Int,
) : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    lateinit var order: Order
        protected set

    fun attach(order: Order) {
        this.order = order
    }

    fun calculateAmount(): Int = price * quantity
}
