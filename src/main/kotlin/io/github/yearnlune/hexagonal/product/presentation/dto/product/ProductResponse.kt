package io.github.yearnlune.hexagonal.product.presentation.dto.product

import io.github.yearnlune.hexagonal.product.domain.vo.ProductStatus

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val status: ProductStatus,
)
