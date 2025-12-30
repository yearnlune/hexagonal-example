package io.github.yearnlune.hexagonal.order.presentation.controller

import io.github.yearnlune.hexagonal.order.application.command.CancelOrderCommand
import io.github.yearnlune.hexagonal.order.application.command.CompleteDeliveryCommand
import io.github.yearnlune.hexagonal.order.application.command.StartShippingCommand
import io.github.yearnlune.hexagonal.order.application.query.GetOrderQuery
import io.github.yearnlune.hexagonal.order.application.query.GetOrdersByUserIdAndStatusQuery
import io.github.yearnlune.hexagonal.order.application.query.GetOrdersByUserIdQuery
import io.github.yearnlune.hexagonal.order.application.service.OrderUseCase
import io.github.yearnlune.hexagonal.order.domain.order.vo.OrderStatus
import io.github.yearnlune.hexagonal.order.presentation.dto.order.CreateOrderRequest
import io.github.yearnlune.hexagonal.order.presentation.dto.order.OrderResponse
import io.github.yearnlune.hexagonal.order.presentation.mapper.toCommand
import io.github.yearnlune.hexagonal.order.presentation.mapper.toResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val orderUseCase: OrderUseCase,
) {
    @PostMapping("/api/orders")
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(
        @RequestBody request: CreateOrderRequest,
    ): OrderResponse = orderUseCase.createOrder(request.toCommand()).toResponse()

    @GetMapping("/api/orders/{orderId}")
    fun getOrder(
        @PathVariable orderId: Long,
    ): OrderResponse = orderUseCase.getOrder(GetOrderQuery(orderId = orderId)).toResponse()

    @GetMapping("/api/orders/users/{userId}")
    fun getOrdersByUserId(
        @PathVariable userId: Long,
    ): List<OrderResponse> =
        orderUseCase.getOrdersByUserId(GetOrdersByUserIdQuery(userId = userId)).map { it.toResponse() }

    @GetMapping("/api/orders/users/{userId}/status/{status}")
    fun getOrdersByUserIdAndStatus(
        @PathVariable userId: Long,
        @PathVariable status: OrderStatus,
    ): List<OrderResponse> =
        orderUseCase
            .getOrdersByUserIdAndStatus(
                GetOrdersByUserIdAndStatusQuery(userId = userId, status = status),
            ).map { it.toResponse() }

    @PostMapping("/api/orders/{orderId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelOrder(
        @PathVariable orderId: Long,
    ) {
        orderUseCase.cancelOrder(CancelOrderCommand(orderId = orderId))
    }

    @PostMapping("/api/orders/{orderId}/shipping/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun startShipping(
        @PathVariable orderId: Long,
    ) {
        orderUseCase.startShipping(StartShippingCommand(orderId = orderId))
    }

    @PostMapping("/api/orders/{orderId}/shipping/complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun completeDelivery(
        @PathVariable orderId: Long,
    ) {
        orderUseCase.completeDelivery(CompleteDeliveryCommand(orderId = orderId))
    }
}
