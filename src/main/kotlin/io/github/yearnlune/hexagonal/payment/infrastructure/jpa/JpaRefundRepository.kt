package io.github.yearnlune.hexagonal.payment.infrastructure.jpa

import io.github.yearnlune.hexagonal.payment.domain.refund.Refund
import org.springframework.data.jpa.repository.JpaRepository

interface JpaRefundRepository : JpaRepository<Refund, Long> {
    fun findByPaymentId(paymentId: Long): List<Refund>

    fun findByIdempotencyKey(idempotencyKey: String): Refund?
}
