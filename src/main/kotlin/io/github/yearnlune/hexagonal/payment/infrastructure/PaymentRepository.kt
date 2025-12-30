package io.github.yearnlune.hexagonal.payment.infrastructure

import io.github.yearnlune.hexagonal.payment.domain.payment.Payment
import io.github.yearnlune.hexagonal.payment.infrastructure.jpa.JpaPaymentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PaymentRepository(
    private val jpa: JpaPaymentRepository,
) {
    fun save(payment: Payment): Payment = jpa.save(payment)

    fun findByIdOrNull(id: Long): Payment? = jpa.findByIdOrNull(id)

    fun findByOrderId(orderId: Long): Payment? = jpa.findByOrderId(orderId)

    fun findByPaymentKey(paymentKey: String): Payment? = jpa.findByPaymentKey(paymentKey)
}
