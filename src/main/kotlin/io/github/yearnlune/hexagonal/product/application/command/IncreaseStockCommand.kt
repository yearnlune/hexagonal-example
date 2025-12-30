package io.github.yearnlune.hexagonal.product.application.command

data class IncreaseStockCommand(
    val productId: Long,
    val quantity: Int,
)
