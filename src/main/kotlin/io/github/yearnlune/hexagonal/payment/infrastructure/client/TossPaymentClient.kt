package io.github.yearnlune.hexagonal.payment.infrastructure.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "tossPaymentClient",
    url = "https://api.tosspayments.com/v1",
    configuration = [TossPaymentFeignConfig::class],
)
interface TossPaymentClient {
    @PostMapping("/payments/confirm")
    fun approvePayment(
        @RequestBody request: TossPaymentRequest,
    ): TossPaymentResponse

    @PostMapping("/payments/{paymentKey}/cancel")
    fun cancelPayment(
        @PathVariable paymentKey: String,
        @RequestHeader("Idempotency-Key") idempotencyKey: String,
        @RequestBody request: TossCancelRequest,
    ): TossPaymentResponse

    @GetMapping("/payments/{paymentKey}")
    fun getPayment(
        @PathVariable paymentKey: String,
    ): TossPaymentResponse

    data class TossPaymentRequest(
        val paymentKey: String,
        val orderId: String,
        val amount: Int,
    )

    data class TossCancelRequest(
        val cancelReason: String,
    )

    data class TossPaymentResponse(
        val paymentKey: String,
        val orderId: String,
        val status: String,
        val totalAmount: Int,
        val method: String? = null,
        val approvedAt: String? = null,
        val canceledAt: String? = null,
    )
}
