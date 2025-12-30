package io.github.yearnlune.hexagonal.payment.presentation.dto.refund

data class RefundRequestKeyResponse(
    val idempotencyKey: String,
    val message: String = "환불 요청 키가 발급되었습니다. 이 키를 사용하여 환불을 처리하세요.",
)
