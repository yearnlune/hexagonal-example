package io.github.yearnlune.hexagonal.payment.domain.payment

import io.github.yearnlune.hexagonal.global.entity.BaseEntity
import io.github.yearnlune.hexagonal.payment.domain.payment.vo.PaymentHistoryType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "payment_histories")
class PaymentHistory(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    val payment: Payment,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val type: PaymentHistoryType,
    @Column(columnDefinition = "TEXT")
    val message: String? = null,
    @Column(length = 500)
    val requestData: String? = null,
    @Column(length = 500)
    val responseData: String? = null,
) : BaseEntity() {
    companion object {
        fun create(
            payment: Payment,
            type: PaymentHistoryType,
            message: String? = null,
            requestData: String? = null,
            responseData: String? = null,
        ): PaymentHistory =
            PaymentHistory(
                payment = payment,
                type = type,
                message = message,
                requestData = requestData,
                responseData = responseData,
            )
    }
}
