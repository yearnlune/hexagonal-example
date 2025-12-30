package io.github.yearnlune.hexagonal.payment.presentation.mapper

import io.github.yearnlune.hexagonal.payment.application.command.CreateRefundCommand
import io.github.yearnlune.hexagonal.payment.application.command.ProcessRefundCommand
import io.github.yearnlune.hexagonal.payment.application.result.RefundRequestKeyResult
import io.github.yearnlune.hexagonal.payment.application.result.RefundResult
import io.github.yearnlune.hexagonal.payment.presentation.dto.refund.CreateRefundRequest
import io.github.yearnlune.hexagonal.payment.presentation.dto.refund.ProcessRefundRequest
import io.github.yearnlune.hexagonal.payment.presentation.dto.refund.RefundRequestKeyResponse
import io.github.yearnlune.hexagonal.payment.presentation.dto.refund.RefundResponse

fun CreateRefundRequest.toCommand() =
    CreateRefundCommand(
        paymentId = paymentId,
        amount = amount,
        reason = reason,
        cancelKey = cancelKey,
        requestedBy = requestedBy,
    )

fun ProcessRefundRequest.toCommand() =
    ProcessRefundCommand(
        idempotencyKey = idempotencyKey,
    )

fun RefundRequestKeyResult.toResponse() =
    RefundRequestKeyResponse(
        idempotencyKey = idempotencyKey,
        message = message,
    )

fun RefundResult.toResponse() =
    RefundResponse(
        id = id,
        paymentId = paymentId,
        amount = amount,
        reason = reason,
        cancelKey = cancelKey,
        idempotencyKey = idempotencyKey,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
