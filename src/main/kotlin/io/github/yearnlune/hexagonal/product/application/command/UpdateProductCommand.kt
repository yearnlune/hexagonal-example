package io.github.yearnlune.hexagonal.product.application.command

data class UpdateProductCommand(
    val name: String? = null,
    val description: String? = null,
    val price: Int? = null,
    val stock: Int? = null,
)
