package io.github.yearnlune.hexagonal.order.presentation.mapper

import io.github.yearnlune.hexagonal.order.application.command.CreateOrderCommand
import io.github.yearnlune.hexagonal.order.application.command.OrderItemCommand
import io.github.yearnlune.hexagonal.order.application.result.OrderItemResult
import io.github.yearnlune.hexagonal.order.application.result.OrderResult
import io.github.yearnlune.hexagonal.order.presentation.dto.order.CreateOrderRequest
import io.github.yearnlune.hexagonal.order.presentation.dto.order.OrderItemRequest
import io.github.yearnlune.hexagonal.order.presentation.dto.order.OrderItemResponse
import io.github.yearnlune.hexagonal.order.presentation.dto.order.OrderResponse

fun CreateOrderRequest.toCommand() =
    CreateOrderCommand(
        userId = userId,
        items = items.map { it.toCommand() },
    )

fun OrderItemRequest.toCommand() =
    OrderItemCommand(
        productId = productId,
        quantity = quantity,
    )

fun OrderResult.toResponse() =
    OrderResponse(
        id = id,
        userId = userId,
        items = items.map { it.toResponse() },
    )

fun OrderItemResult.toResponse() =
    OrderItemResponse(
        productId = productId,
        productName = productName,
        price = price,
        quantity = quantity,
    )
