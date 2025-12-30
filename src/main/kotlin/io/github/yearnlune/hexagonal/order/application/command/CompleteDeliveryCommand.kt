package io.github.yearnlune.hexagonal.order.application.command

data class CompleteDeliveryCommand(
    val orderId: Long,
)
