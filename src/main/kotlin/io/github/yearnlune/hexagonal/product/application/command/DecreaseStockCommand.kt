package io.github.yearnlune.hexagonal.product.application.command

data class DecreaseStockCommand(
    val productId: Long,
    val quantity: Int,
)
