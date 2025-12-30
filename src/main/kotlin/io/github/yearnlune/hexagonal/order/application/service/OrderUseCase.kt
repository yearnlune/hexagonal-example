package io.github.yearnlune.hexagonal.order.application.service

import io.github.yearnlune.hexagonal.global.exception.BusinessException
import io.github.yearnlune.hexagonal.global.exception.ErrorCode
import io.github.yearnlune.hexagonal.order.application.command.CancelOrderCommand
import io.github.yearnlune.hexagonal.order.application.command.CompleteDeliveryCommand
import io.github.yearnlune.hexagonal.order.application.command.CreateOrderCommand
import io.github.yearnlune.hexagonal.order.application.command.StartShippingCommand
import io.github.yearnlune.hexagonal.order.application.query.GetOrderQuery
import io.github.yearnlune.hexagonal.order.application.query.GetOrdersByUserIdAndStatusQuery
import io.github.yearnlune.hexagonal.order.application.query.GetOrdersByUserIdQuery
import io.github.yearnlune.hexagonal.order.application.result.OrderResult
import io.github.yearnlune.hexagonal.order.domain.order.Order
import io.github.yearnlune.hexagonal.order.domain.order.OrderItem
import io.github.yearnlune.hexagonal.order.infrastructure.OrderRepository
import io.github.yearnlune.hexagonal.product.application.command.DecreaseStockCommand
import io.github.yearnlune.hexagonal.product.application.command.IncreaseStockCommand
import io.github.yearnlune.hexagonal.product.application.query.GetProductSnapshotQuery
import io.github.yearnlune.hexagonal.product.application.service.ProductCommandService
import io.github.yearnlune.hexagonal.product.application.service.ProductQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OrderUseCase(
    private val orderRepository: OrderRepository,
    private val productQueryService: ProductQueryService,
    private val productCommandService: ProductCommandService,
) {
    @Transactional
    fun createOrder(command: CreateOrderCommand): OrderResult {
        val orderItems =
            command.items.map { itemCommand ->
                val productSnapshot =
                    productQueryService.getProductSnapshot(GetProductSnapshotQuery(productId = itemCommand.productId))
                productCommandService.decreaseStock(
                    DecreaseStockCommand(
                        productId = itemCommand.productId,
                        quantity = itemCommand.quantity,
                    ),
                )

                OrderItem(
                    productId = productSnapshot.id,
                    productName = productSnapshot.name,
                    price = productSnapshot.price,
                    quantity = itemCommand.quantity,
                )
            }

        val order =
            Order.create(
                userId = command.userId,
                items = orderItems,
            )

        order.confirm()

        val savedOrder = orderRepository.save(order)
        return OrderResult.from(savedOrder)
    }

    fun getOrder(query: GetOrderQuery): OrderResult {
        val order =
            orderRepository.findByIdOrNull(query.orderId)
                ?: throw BusinessException(
                    ErrorCode.ORDER_NOT_FOUND,
                    params = mapOf("orderId" to query.orderId),
                )

        return OrderResult.from(order)
    }

    fun getOrdersByUserId(query: GetOrdersByUserIdQuery): List<OrderResult> =
        orderRepository
            .findByUserId(query.userId)
            .map { OrderResult.from(it) }

    fun getOrdersByUserIdAndStatus(query: GetOrdersByUserIdAndStatusQuery): List<OrderResult> =
        orderRepository
            .findByUserIdAndStatus(query.userId, query.status)
            .map { OrderResult.from(it) }

    @Transactional
    fun cancelOrder(command: CancelOrderCommand) {
        val order =
            orderRepository.findByIdOrNull(command.orderId)
                ?: throw BusinessException(
                    ErrorCode.ORDER_NOT_FOUND,
                    params = mapOf("orderId" to command.orderId),
                )

        order.cancel()

        order.items.forEach { item ->
            productCommandService.increaseStock(
                IncreaseStockCommand(
                    productId = item.productId,
                    quantity = item.quantity,
                ),
            )
        }
    }

    @Transactional
    fun startShipping(command: StartShippingCommand) {
        val order =
            orderRepository.findByIdOrNull(command.orderId)
                ?: throw BusinessException(
                    ErrorCode.ORDER_NOT_FOUND,
                    params = mapOf("orderId" to command.orderId),
                )

        order.startShipping()
    }

    @Transactional
    fun completeDelivery(command: CompleteDeliveryCommand) {
        val order =
            orderRepository.findByIdOrNull(command.orderId)
                ?: throw BusinessException(
                    ErrorCode.ORDER_NOT_FOUND,
                    params = mapOf("orderId" to command.orderId),
                )

        order.completeDelivery()
    }
}
