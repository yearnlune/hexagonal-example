package io.github.yearnlune.hexagonal.product.presentation.controller

import io.github.yearnlune.hexagonal.product.application.query.GetProductQuery
import io.github.yearnlune.hexagonal.product.application.service.ProductUseCase
import io.github.yearnlune.hexagonal.product.presentation.dto.product.CreateProductRequest
import io.github.yearnlune.hexagonal.product.presentation.dto.product.ProductResponse
import io.github.yearnlune.hexagonal.product.presentation.dto.product.UpdateProductRequest
import io.github.yearnlune.hexagonal.product.presentation.mapper.toCommand
import io.github.yearnlune.hexagonal.product.presentation.mapper.toResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productUseCase: ProductUseCase,
) {
    @PostMapping("/api/products")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(
        @RequestBody request: CreateProductRequest,
    ): ProductResponse = productUseCase.createProduct(request.toCommand()).toResponse()

    @GetMapping("/api/products/{productId}")
    fun getProduct(
        @PathVariable productId: Long,
    ): ProductResponse = productUseCase.getProduct(GetProductQuery(productId = productId)).toResponse()

    @GetMapping("/api/products")
    fun getAllProducts(): List<ProductResponse> = productUseCase.getAllProducts().map { it.toResponse() }

    @GetMapping("/api/products/active")
    fun getActiveProducts(): List<ProductResponse> = productUseCase.getActiveProducts().map { it.toResponse() }

    @PutMapping("/api/products/{productId}")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestBody request: UpdateProductRequest,
    ): ProductResponse = productUseCase.updateProduct(productId, request.toCommand()).toResponse()

    @DeleteMapping("/api/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(
        @PathVariable productId: Long,
    ) {
        productUseCase.deleteProduct(productId)
    }
}
