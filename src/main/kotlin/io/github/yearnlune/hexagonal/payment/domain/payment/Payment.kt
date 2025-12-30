package io.github.yearnlune.hexagonal.payment.domain.payment

import io.github.yearnlune.hexagonal.global.entity.BaseEntity
import io.github.yearnlune.hexagonal.payment.domain.payment.vo.PaymentStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "payments")
class Payment(
    @Column(nullable = false)
    val orderId: Long,
    @Column(nullable = false)
    val amount: Int,
    @Column(nullable = false, length = 100)
    val paymentKey: String,
    @Column(nullable = false, length = 100)
    val orderName: String,
    status: PaymentStatus = PaymentStatus.PENDING,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: PaymentStatus = status
        protected set

    fun approve() {
        if (status != PaymentStatus.PENDING) {
            throw IllegalStateException("대기 중인 결제만 승인할 수 있습니다")
        }
        status = PaymentStatus.APPROVED
    }

    fun fail() {
        if (status != PaymentStatus.PENDING) {
            throw IllegalStateException("대기 중인 결제만 실패 처리할 수 있습니다")
        }
        status = PaymentStatus.FAILED
    }

    fun cancel() {
        if (status != PaymentStatus.APPROVED) {
            throw IllegalStateException("승인된 결제만 취소할 수 있습니다")
        }
        status = PaymentStatus.CANCELLED
    }

    companion object {
        fun create(
            orderId: Long,
            amount: Int,
            paymentKey: String,
            orderName: String,
        ): Payment =
            Payment(
                orderId = orderId,
                amount = amount,
                paymentKey = paymentKey,
                orderName = orderName,
            )
    }
}
