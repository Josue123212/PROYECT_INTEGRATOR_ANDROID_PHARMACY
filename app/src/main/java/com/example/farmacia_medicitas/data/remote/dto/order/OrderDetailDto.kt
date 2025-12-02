package com.example.farmacia_medicitas.data.remote.dto.order

data class OrderItemDto(
    val product_title: String?,
    val quantity: Int?,
    val subtotal: Double?
)

data class OrderDetailDto(
    val id: Int?,
    val total: Double?,
    val currency: String?,
    val status: String?,
    val created_at: String?,
    val items: List<OrderItemDto>?
)

