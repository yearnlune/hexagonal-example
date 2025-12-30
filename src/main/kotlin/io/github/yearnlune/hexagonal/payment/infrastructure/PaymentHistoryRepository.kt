package io.github.yearnlune.hexagonal.payment.infrastructure

import io.github.yearnlune.hexagonal.payment.domain.payment.PaymentHistory
import io.github.yearnlune.hexagonal.payment.infrastructure.jpa.JpaPaymentHistoryRepository
import org.springframework.stereotype.Repository

@Repository
class PaymentHistoryRepository(
    private val jpa: JpaPaymentHistoryRepository,
) {
    fun save(history: PaymentHistory): PaymentHistory = jpa.save(history)

    fun findByPaymentIdOrderByCreatedAtDesc(paymentId: Long): List<PaymentHistory> =
        jpa.findByPaymentIdOrderByCreatedAtDesc(paymentId)
}
