package com.example.farmacia_medicitas.data.remote.dto.cart

import com.example.farmacia_medicitas.data.remote.dto.ProductDto

data class CartItemDto(
    val id: Int,
    val product: Int,
    val product_detail: ProductDto?,
    val quantity: Int,
    val unit_price: String,
    val subtotal: String
)

data class CartDto(
    val id: Int,
    val user: Int,
    val updated_at: String,
    val items: List<CartItemDto>
)
