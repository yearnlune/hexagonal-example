package io.github.yearnlune.hexagonal.product.presentation.mapper

import io.github.yearnlune.hexagonal.product.application.command.CreateProductCommand
import io.github.yearnlune.hexagonal.product.application.command.UpdateProductCommand
import io.github.yearnlune.hexagonal.product.application.result.ProductResult
import io.github.yearnlune.hexagonal.product.presentation.dto.product.CreateProductRequest
import io.github.yearnlune.hexagonal.product.presentation.dto.product.ProductResponse
import io.github.yearnlune.hexagonal.product.presentation.dto.product.UpdateProductRequest

fun CreateProductRequest.toCommand() =
    CreateProductCommand(
        name = name,
        description = description,
        price = price,
        stock = stock,
    )

fun UpdateProductRequest.toCommand() =
    UpdateProductCommand(
        name = name,
        description = description,
        price = price,
        stock = stock,
    )

fun ProductResult.toResponse() =
    ProductResponse(
        id = id,
        name = name,
        description = description,
        price = price,
        stock = stock,
        status = status,
    )
