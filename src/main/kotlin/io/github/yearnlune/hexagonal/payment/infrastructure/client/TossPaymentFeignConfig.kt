package io.github.yearnlune.hexagonal.payment.infrastructure.client

import feign.RequestInterceptor
import java.util.Base64
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TossPaymentFeignConfig(
    @Value("\${toss.secret-key:}")
    private val secretKey: String,
) {
    @Bean
    fun requestInterceptor(): RequestInterceptor =
        RequestInterceptor { template ->
            val auth = "Basic ${Base64.getEncoder().encodeToString("$secretKey:".toByteArray())}"
            template.header("Authorization", auth)
            template.header("Content-Type", "application/json")
        }
}
