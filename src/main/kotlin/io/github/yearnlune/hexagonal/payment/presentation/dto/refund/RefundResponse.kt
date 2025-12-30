package io.github.yearnlune.hexagonal.payment.presentation.dto.refund

import io.github.yearnlune.hexagonal.payment.domain.refund.vo.RefundStatus
import java.time.LocalDateTime

data class RefundResponse(
    val id: Long,
    val paymentId: Long,
    val amount: Int,
    val reason: String,
    val cancelKey: String,
    val idempotencyKey: String,
    val status: RefundStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
