package io.github.yearnlune.hexagonal.payment.infrastructure.jpa

import io.github.yearnlune.hexagonal.payment.domain.payment.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPaymentRepository : JpaRepository<Payment, Long> {
    fun findByOrderId(orderId: Long): Payment?

    fun findByPaymentKey(paymentKey: String): Payment?
}
