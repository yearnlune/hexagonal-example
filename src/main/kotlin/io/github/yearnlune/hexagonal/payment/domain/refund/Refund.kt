package io.github.yearnlune.hexagonal.payment.domain.refund

import io.github.yearnlune.hexagonal.global.entity.BaseEntity
import io.github.yearnlune.hexagonal.payment.domain.refund.vo.RefundStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "refunds",
    uniqueConstraints = [UniqueConstraint(name = "uk_refund_idempotency_key", columnNames = ["idempotencyKey"])],
)
class Refund(
    @Column(nullable = false)
    val paymentId: Long,
    @Column(nullable = false)
    val amount: Int,
    @Column(nullable = false, length = 200)
    val reason: String,
    @Column(nullable = false, length = 100)
    val cancelKey: String,
    @Column(nullable = false, unique = true, length = 100)
    val idempotencyKey: String,
    status: RefundStatus = RefundStatus.PENDING,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: RefundStatus = status
        protected set

    fun approve() {
        if (status != RefundStatus.PENDING) {
            throw IllegalStateException("대기 중인 환불만 승인할 수 있습니다")
        }
        status = RefundStatus.APPROVED
    }

    fun fail() {
        if (status != RefundStatus.PENDING) {
            throw IllegalStateException("대기 중인 환불만 실패 처리할 수 있습니다")
        }
        status = RefundStatus.FAILED
    }

    companion object {
        fun create(
            paymentId: Long,
            amount: Int,
            reason: String,
            cancelKey: String,
            idempotencyKey: String,
        ): Refund =
            Refund(
                paymentId = paymentId,
                amount = amount,
                reason = reason,
                cancelKey = cancelKey,
                idempotencyKey = idempotencyKey,
            )
    }
}
