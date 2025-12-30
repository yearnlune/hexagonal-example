package io.github.yearnlune.hexagonal.payment.presentation.controller

import io.github.yearnlune.hexagonal.payment.application.query.GetPaymentByOrderIdQuery
import io.github.yearnlune.hexagonal.payment.application.query.GetPaymentQuery
import io.github.yearnlune.hexagonal.payment.application.service.PaymentUseCase
import io.github.yearnlune.hexagonal.payment.presentation.dto.payment.ApprovePaymentRequest
import io.github.yearnlune.hexagonal.payment.presentation.dto.payment.CreatePaymentRequest
import io.github.yearnlune.hexagonal.payment.presentation.dto.payment.PaymentResponse
import io.github.yearnlune.hexagonal.payment.presentation.mapper.toCommand
import io.github.yearnlune.hexagonal.payment.presentation.mapper.toResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController(
    private val paymentUseCase: PaymentUseCase,
) {
    @PostMapping("/api/payments")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPayment(
        @RequestBody request: CreatePaymentRequest,
    ): PaymentResponse = paymentUseCase.createPayment(request.toCommand()).toResponse()

    @PostMapping("/api/payments/approve")
    fun approvePayment(
        @RequestBody request: ApprovePaymentRequest,
    ): PaymentResponse = paymentUseCase.approvePayment(request.toCommand()).toResponse()

    @GetMapping("/api/payments/{paymentId}")
    fun getPayment(
        @PathVariable paymentId: Long,
    ): PaymentResponse = paymentUseCase.getPayment(GetPaymentQuery(paymentId = paymentId)).toResponse()

    @GetMapping("/api/payments/orders/{orderId}")
    fun getPaymentByOrderId(
        @PathVariable orderId: Long,
    ): PaymentResponse = paymentUseCase.getPaymentByOrderId(GetPaymentByOrderIdQuery(orderId = orderId)).toResponse()
}
