package io.github.yearnlune.hexagonal.product.infrastructure

import io.github.yearnlune.hexagonal.product.domain.Product
import io.github.yearnlune.hexagonal.product.domain.vo.ProductStatus
import org.springframework.data.jpa.repository.JpaRepository

interface JpaProductRepository : JpaRepository<Product, Long> {
    fun findAllByStatus(status: ProductStatus): List<Product>
}
