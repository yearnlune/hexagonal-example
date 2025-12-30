package io.github.yearnlune.hexagonal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class HexagonalExampleApplication

fun main(args: Array<String>) {
    runApplication<HexagonalExampleApplication>(*args)
}
