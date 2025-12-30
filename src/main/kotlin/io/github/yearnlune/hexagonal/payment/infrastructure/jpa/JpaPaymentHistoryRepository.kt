package io.github.yearnlune.hexagonal.payment.infrastructure.jpa

import io.github.yearnlune.hexagonal.payment.domain.payment.PaymentHistory
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPaymentHistoryRepository : JpaRepository<PaymentHistory, Long> {
    fun findByPaymentIdOrderByCreatedAtDesc(paymentId: Long): List<PaymentHistory>
}
