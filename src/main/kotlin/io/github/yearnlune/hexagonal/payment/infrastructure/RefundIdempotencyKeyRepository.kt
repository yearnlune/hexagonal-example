package io.github.yearnlune.hexagonal.payment.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.math.BigDecimal
import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RefundIdempotencyKeyRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    private val objectMapper =
        ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
        }

    companion object {
        private const val REFUND_IDEMPOTENCY_KEY_PREFIX = "refund:idempotency:"
        private const val REFUND_REQUEST_KEY_PREFIX = "refund:request:"
        private const val DEFAULT_TTL_HOURS = 24L
        private const val IDEMPOTENCY_KEY_COMPONENTS_SEPARATOR = ":"
    }

    fun generateIdempotencyKey(
        idempotencyKeyComponent: String,
        paymentKey: String,
        refundAmount: BigDecimal,
        refundReason: String,
        requestedBy: Long,
    ): String {
        val keyComponents =
            listOf(
                idempotencyKeyComponent,
                paymentKey,
                refundAmount.toString(),
                refundReason,
                requestedBy.toString(),
            ).joinToString(IDEMPOTENCY_KEY_COMPONENTS_SEPARATOR)

        return MessageDigest
            .getInstance("SHA-256")
            .digest(keyComponents.toByteArray())
            .joinToString("", transform = "%02x"::format)
    }

    private fun generateRequestKey(
        paymentId: Long,
        amount: Int,
        reason: String,
    ): String = "$REFUND_REQUEST_KEY_PREFIX$paymentId:$amount:${reason.hashCode()}"

    fun saveIdempotencyKey(
        paymentId: Long,
        amount: Int,
        reason: String,
        cancelKey: String,
        idempotencyKey: String,
        ttlHours: Long = DEFAULT_TTL_HOURS,
    ): String? {
        val requestKey = generateRequestKey(paymentId, amount, reason)

        val previousKey = redisTemplate.opsForValue().get(requestKey)
        if (previousKey != null) {
            redisTemplate.delete("$REFUND_IDEMPOTENCY_KEY_PREFIX$previousKey")
        }

        val keyData = IdempotencyKeyData(paymentId, amount, reason, cancelKey)
        val jsonData = objectMapper.writeValueAsString(keyData)

        redisTemplate.opsForValue().set(
            "$REFUND_IDEMPOTENCY_KEY_PREFIX$idempotencyKey",
            jsonData,
            ttlHours,
            TimeUnit.HOURS,
        )

        redisTemplate.opsForValue().set(
            requestKey,
            idempotencyKey,
            ttlHours,
            TimeUnit.HOURS,
        )

        return previousKey
    }

    fun getIdempotencyKeyData(idempotencyKey: String): IdempotencyKeyData? {
        val jsonData =
            redisTemplate.opsForValue().get("$REFUND_IDEMPOTENCY_KEY_PREFIX$idempotencyKey")
                ?: return null

        return try {
            objectMapper.readValue(jsonData, IdempotencyKeyData::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun exists(idempotencyKey: String): Boolean =
        redisTemplate.hasKey("$REFUND_IDEMPOTENCY_KEY_PREFIX$idempotencyKey") ?: false

    fun delete(idempotencyKey: String) {
        redisTemplate.delete("$REFUND_IDEMPOTENCY_KEY_PREFIX$idempotencyKey")
    }

    fun extendTtl(
        idempotencyKey: String,
        ttlHours: Long = DEFAULT_TTL_HOURS,
    ) {
        redisTemplate.expire(
            "$REFUND_IDEMPOTENCY_KEY_PREFIX$idempotencyKey",
            ttlHours,
            TimeUnit.HOURS,
        )
    }

    data class IdempotencyKeyData(
        val paymentId: Long,
        val amount: Int,
        val reason: String,
        val cancelKey: String,
    )
}
