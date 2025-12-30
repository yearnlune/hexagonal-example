package io.github.yearnlune.hexagonal.product.application.service

import io.github.yearnlune.hexagonal.global.exception.BusinessException
import io.github.yearnlune.hexagonal.global.exception.ErrorCode
import io.github.yearnlune.hexagonal.product.application.command.CreateProductCommand
import io.github.yearnlune.hexagonal.product.application.command.DecreaseStockCommand
import io.github.yearnlune.hexagonal.product.application.command.IncreaseStockCommand
import io.github.yearnlune.hexagonal.product.application.command.UpdateProductCommand
import io.github.yearnlune.hexagonal.product.application.query.GetProductQuery
import io.github.yearnlune.hexagonal.product.application.result.ProductResult
import io.github.yearnlune.hexagonal.product.domain.Product
import io.github.yearnlune.hexagonal.product.domain.vo.ProductStatus
import io.github.yearnlune.hexagonal.product.infrastructure.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductUseCase(
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun createProduct(command: CreateProductCommand): ProductResult {
        val product =
            Product(
                name = command.name,
                description = command.description,
                price = command.price,
                stock = command.stock,
            )

        val savedProduct = productRepository.save(product)
        return ProductResult.from(savedProduct)
    }

    fun getProduct(query: GetProductQuery): ProductResult {
        val product =
            productRepository.findByIdOrNull(query.productId)
                ?: throw BusinessException(
                    ErrorCode.PRODUCT_NOT_FOUND,
                    params = mapOf("productId" to query.productId),
                )

        return ProductResult.from(product)
    }

    fun getAllProducts(): List<ProductResult> =
        productRepository
            .findAll()
            .map { ProductResult.from(it) }

    fun getActiveProducts(): List<ProductResult> =
        productRepository
            .findAllByStatus(ProductStatus.ACTIVE)
            .map { ProductResult.from(it) }

    @Transactional
    fun updateProduct(
        productId: Long,
        command: UpdateProductCommand,
    ): ProductResult {
        val product =
            productRepository.findByIdOrNull(productId)
                ?: throw BusinessException(
                    ErrorCode.PRODUCT_NOT_FOUND,
                    params = mapOf("productId" to productId),
                )

        product.update(
            name = command.name,
            description = command.description,
            price = command.price,
            stock = command.stock,
        )

        return ProductResult.from(product)
    }

    @Transactional
    fun deleteProduct(productId: Long) {
        val product =
            productRepository.findByIdOrNull(productId)
                ?: throw BusinessException(
                    ErrorCode.PRODUCT_NOT_FOUND,
                    params = mapOf("productId" to productId),
                )

        product.markAsDeleted()
    }

    @Transactional
    fun decreaseStock(command: DecreaseStockCommand) {
        val product =
            productRepository.findByIdOrNull(command.productId)
                ?: throw BusinessException(
                    ErrorCode.PRODUCT_NOT_FOUND,
                    params = mapOf("productId" to command.productId),
                )

        product.decreaseStock(command.quantity)
    }

    @Transactional
    fun increaseStock(command: IncreaseStockCommand) {
        val product =
            productRepository.findByIdOrNull(command.productId)
                ?: throw BusinessException(
                    ErrorCode.PRODUCT_NOT_FOUND,
                    params = mapOf("productId" to command.productId),
                )

        product.increaseStock(command.quantity)
    }
}
