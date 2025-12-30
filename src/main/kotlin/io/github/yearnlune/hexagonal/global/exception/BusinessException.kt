package io.github.yearnlune.hexagonal.global.exception

class BusinessException(
    val errorCode: ErrorCode,
    val params: Map<String, Any> = emptyMap(),
    override val message: String = errorCode.message,
) : RuntimeException(message)

enum class ErrorCode(
    val message: String,
) {
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다"),
    INSUFFICIENT_STOCK("재고가 부족합니다"),

    ORDER_NOT_FOUND("주문을 찾을 수 없습니다"),
    INVALID_ORDER_STATUS("유효하지 않은 주문 상태입니다"),
    EMPTY_ORDER_ITEMS("주문 항목이 비어있습니다"),

    PAYMENT_NOT_FOUND("결제를 찾을 수 없습니다"),
    PAYMENT_APPROVE_FAILED("결제 승인에 실패했습니다"),
    PAYMENT_CANCEL_FAILED("결제 취소에 실패했습니다"),
    REFUND_NOT_FOUND("환불을 찾을 수 없습니다"),
    REFUND_REQUEST_KEY_NOT_FOUND("환불 요청 키를 찾을 수 없습니다"),

    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다"),
}
