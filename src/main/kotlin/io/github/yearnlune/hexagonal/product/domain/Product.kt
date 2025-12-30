package io.github.yearnlune.hexagonal.product.domain

import io.github.yearnlune.hexagonal.global.entity.BaseEntity
import io.github.yearnlune.hexagonal.global.exception.BusinessException
import io.github.yearnlune.hexagonal.global.exception.ErrorCode
import io.github.yearnlune.hexagonal.product.domain.vo.ProductStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class Product(
    name: String,
    description: String,
    price: Int,
    stock: Int = 0,
    status: ProductStatus = ProductStatus.ACTIVE,
) : BaseEntity() {
    @Column(nullable = false, length = 200)
    var name: String = name
        protected set

    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String = description
        protected set

    @Column(nullable = false)
    var price: Int = price
        protected set

    @Column(nullable = false)
    var stock: Int = stock
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: ProductStatus = status
        protected set

    fun decreaseStock(quantity: Int) {
        if (stock < quantity) {
            throw BusinessException(
                ErrorCode.INSUFFICIENT_STOCK,
                params =
                    mapOf(
                        "productId" to id,
                        "requested" to quantity,
                        "available" to stock,
                    ),
            )
        }
        stock -= quantity
    }

    fun increaseStock(quantity: Int) {
        stock += quantity
    }

    fun activate() {
        status = ProductStatus.ACTIVE
    }

    fun deactivate() {
        status = ProductStatus.INACTIVE
    }

    fun markAsSoldOut() {
        status = ProductStatus.SOLD_OUT
    }

    fun update(
        name: String? = null,
        description: String? = null,
        price: Int? = null,
        stock: Int? = null,
    ) {
        name?.let { this.name = it }
        description?.let { this.description = it }
        price?.let { this.price = it }
        stock?.let { this.stock = it }
    }

    fun markAsDeleted() {
        isDeleted = true
    }
}
