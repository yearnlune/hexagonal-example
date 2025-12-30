package io.github.yearnlune.hexagonal.payment.domain.refund.vo

enum class RefundReasonType(
    val description: String,
) {
    CUSTOMER_REQUEST("고객 요청"),
    PRODUCT_DEFECT("상품 불량"),
    DELIVERY_DELAY("배송 지연"),
    WRONG_ORDER("주문 오류"),
    DUPLICATE_PAYMENT("중복 결제"),
    OTHER("기타"),
    ;

    companion object {
        fun fromString(reason: String): RefundReasonType = values().find { it.description == reason } ?: OTHER
    }
}
