package io.github.yearnlune.hexagonal.product.presentation.dto.product

data class CreateProductRequest(
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
)
