package io.github.yearnlune.hexagonal.product.application.result

data class ProductSnapshotResult(
    val id: Long,
    val name: String,
    val price: Int,
    val stock: Int,
)
