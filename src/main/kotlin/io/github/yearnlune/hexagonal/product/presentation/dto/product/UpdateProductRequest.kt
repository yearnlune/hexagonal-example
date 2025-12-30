package io.github.yearnlune.hexagonal.product.presentation.dto.product

data class UpdateProductRequest(
    val name: String? = null,
    val description: String? = null,
    val price: Int? = null,
    val stock: Int? = null,
)
