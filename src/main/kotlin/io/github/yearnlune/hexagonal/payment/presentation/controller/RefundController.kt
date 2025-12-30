package io.github.yearnlune.hexagonal.payment.presentation.controller

import io.github.yearnlune.hexagonal.payment.application.query.GetRefundQuery
import io.github.yearnlune.hexagonal.payment.application.query.GetRefundsByPaymentIdQuery
import io.github.yearnlune.hexagonal.payment.application.service.RefundUseCase
import io.github.yearnlune.hexagonal.payment.presentation.dto.refund.CreateRefundRequest
import io.github.yearnlune.hexagonal.payment.presentation.dto.refund.ProcessRefundRequest
import io.github.yearnlune.hexagonal.payment.presentation.dto.refund.RefundRequestKeyResponse
import io.github.yearnlune.hexagonal.payment.presentation.dto.refund.RefundResponse
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
class RefundController(
    private val refundUseCase: RefundUseCase,
) {
    @PostMapping("/api/refunds/request-key")
    @ResponseStatus(HttpStatus.CREATED)
    fun generateRefundRequestKey(
        @RequestBody request: CreateRefundRequest,
    ): RefundRequestKeyResponse = refundUseCase.generateRefundRequestKey(request.toCommand()).toResponse()

    @PostMapping("/api/refunds/process")
    fun processRefundWithKey(
        @RequestBody request: ProcessRefundRequest,
    ): RefundResponse = refundUseCase.processRefundWithKey(request.toCommand()).toResponse()

    @GetMapping("/api/refunds/{refundId}")
    fun getRefund(
        @PathVariable refundId: Long,
    ): RefundResponse = refundUseCase.getRefund(GetRefundQuery(refundId = refundId)).toResponse()

    @GetMapping("/api/refunds/payments/{paymentId}")
    fun getRefundsByPaymentId(
        @PathVariable paymentId: Long,
    ): List<RefundResponse> =
        refundUseCase
            .getRefundsByPaymentId(GetRefundsByPaymentIdQuery(paymentId = paymentId))
            .map { it.toResponse() }
}
