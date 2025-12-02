package com.example.farmacia_medicitas.data.mappers

import com.example.farmacia_medicitas.core.ensureAbsoluteUrl
import com.example.farmacia_medicitas.data.model.Product
import com.example.farmacia_medicitas.data.remote.dto.ProductDto

fun ProductDto.toDomain(): Product {
    val saleActive = this.price?.sale_active == true
    val base = this.price?.amount ?: 0.0
    val sale = this.price?.sale_amount
    val priceValue = if (saleActive && sale != null && sale < base) sale else base
    val imagePath = this.images?.firstOrNull()?.image
    return Product(
        id = this.id.toString(),
        name = this.name,
        description = this.description ?: "",
        price = priceValue,
        basePrice = base,
        salePrice = sale,
        saleActive = saleActive,
        imageUrl = imagePath?.let { ensureAbsoluteUrl(it) },
        inStock = true
    )
}
