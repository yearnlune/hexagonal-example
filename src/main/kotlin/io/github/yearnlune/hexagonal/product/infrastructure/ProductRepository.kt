package io.github.yearnlune.hexagonal.product.infrastructure

import io.github.yearnlune.hexagonal.product.domain.Product
import io.github.yearnlune.hexagonal.product.domain.vo.ProductStatus
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    private val jpa: JpaProductRepository,
) {
    fun save(product: Product): Product = jpa.save(product)

    fun findByIdOrNull(id: Long): Product? = jpa.findByIdOrNull(id)

    fun findAll(): List<Product> = jpa.findAll()

    fun findAllByStatus(status: ProductStatus): List<Product> = jpa.findAllByStatus(status)

    fun deleteById(id: Long) = jpa.deleteById(id)
}
