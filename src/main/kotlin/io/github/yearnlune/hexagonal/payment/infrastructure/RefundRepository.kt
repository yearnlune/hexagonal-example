package io.github.yearnlune.hexagonal.payment.infrastructure

import io.github.yearnlune.hexagonal.payment.domain.refund.Refund
import io.github.yearnlune.hexagonal.payment.infrastructure.jpa.JpaRefundRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class RefundRepository(
    private val jpa: JpaRefundRepository,
) {
    fun save(refund: Refund): Refund = jpa.save(refund)

    fun findByIdOrNull(id: Long): Refund? = jpa.findByIdOrNull(id)

    fun findByPaymentId(paymentId: Long): List<Refund> = jpa.findByPaymentId(paymentId)

    fun findByIdempotencyKey(idempotencyKey: String): Refund? = jpa.findByIdempotencyKey(idempotencyKey)

    fun delete(refund: Refund) = jpa.delete(refund)
}
