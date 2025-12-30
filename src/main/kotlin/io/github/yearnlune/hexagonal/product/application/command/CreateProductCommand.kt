package io.github.yearnlune.hexagonal.product.application.command

data class CreateProductCommand(
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
)
