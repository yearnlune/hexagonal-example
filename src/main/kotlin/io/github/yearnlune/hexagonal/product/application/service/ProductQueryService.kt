package io.github.yearnlune.hexagonal.product.application.service

import io.github.yearnlune.hexagonal.global.exception.BusinessException
import io.github.yearnlune.hexagonal.global.exception.ErrorCode
import io.github.yearnlune.hexagonal.product.application.query.GetProductSnapshotQuery
import io.github.yearnlune.hexagonal.product.application.result.ProductSnapshotResult
import io.github.yearnlune.hexagonal.product.infrastructure.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductQueryService(
    private val productRepository: ProductRepository,
) {
    fun getProductSnapshot(query: GetProductSnapshotQuery): ProductSnapshotResult {
        val product =
            productRepository.findByIdOrNull(query.productId)
                ?: throw BusinessException(
                    ErrorCode.PRODUCT_NOT_FOUND,
                    params = mapOf("productId" to query.productId),
                )

        return ProductSnapshotResult(
            id = product.id,
            name = product.name,
            price = product.price,
            stock = product.stock,
        )
    }
}
