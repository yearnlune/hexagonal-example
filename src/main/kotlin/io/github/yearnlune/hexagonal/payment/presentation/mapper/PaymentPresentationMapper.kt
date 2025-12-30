package io.github.yearnlune.hexagonal.payment.presentation.mapper

import io.github.yearnlune.hexagonal.payment.application.command.ApprovePaymentCommand
import io.github.yearnlune.hexagonal.payment.application.command.CancelPaymentCommand
import io.github.yearnlune.hexagonal.payment.application.command.CreatePaymentCommand
import io.github.yearnlune.hexagonal.payment.application.result.PaymentResult
import io.github.yearnlune.hexagonal.payment.presentation.dto.payment.ApprovePaymentRequest
import io.github.yearnlune.hexagonal.payment.presentation.dto.payment.CancelPaymentRequest
import io.github.yearnlune.hexagonal.payment.presentation.dto.payment.CreatePaymentRequest
import io.github.yearnlune.hexagonal.payment.presentation.dto.payment.PaymentResponse

fun CreatePaymentRequest.toCommand() =
    CreatePaymentCommand(
        orderId = orderId,
        amount = amount,
        paymentKey = paymentKey,
        orderName = orderName,
    )

fun ApprovePaymentRequest.toCommand() =
    ApprovePaymentCommand(
        paymentKey = paymentKey,
        orderId = orderId,
        amount = amount,
    )

fun CancelPaymentRequest.toCommand() =
    CancelPaymentCommand(
        paymentKey = paymentKey,
        cancelReason = cancelReason,
    )

fun PaymentResult.toResponse() =
    PaymentResponse(
        id = id,
        orderId = orderId,
        amount = amount,
        paymentKey = paymentKey,
        orderName = orderName,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
