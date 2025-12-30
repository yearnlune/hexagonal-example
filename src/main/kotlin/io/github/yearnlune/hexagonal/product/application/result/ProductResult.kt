package io.github.yearnlune.hexagonal.product.application.result

import io.github.yearnlune.hexagonal.product.domain.Product
import io.github.yearnlune.hexagonal.product.domain.vo.ProductStatus

data class ProductResult(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val status: ProductStatus,
) {
    companion object {
        fun from(product: Product) =
            ProductResult(
                id = product.id,
                name = product.name,
                description = product.description,
                price = product.price,
                stock = product.stock,
                status = product.status,
            )
    }
}
