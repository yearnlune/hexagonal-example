package io.github.yearnlune.hexagonal.product.application.service

import io.github.yearnlune.hexagonal.global.exception.BusinessException
import io.github.yearnlune.hexagonal.global.exception.ErrorCode
import io.github.yearnlune.hexagonal.product.application.command.DecreaseStockCommand
import io.github.yearnlune.hexagonal.product.application.command.IncreaseStockCommand
import io.github.yearnlune.hexagonal.product.infrastructure.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductCommandService(
    private val productRepository: ProductRepository,
) {
    fun decreaseStock(command: DecreaseStockCommand) {
        val product =
            productRepository.findByIdOrNull(command.productId)
                ?: throw BusinessException(
                    ErrorCode.PRODUCT_NOT_FOUND,
                    params = mapOf("productId" to command.productId),
                )

        product.decreaseStock(command.quantity)
    }

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
