package com.example.farmacia_medicitas.data.remote.dto.order

data class OrderDto(
    val id: Int,
    val total: Double,
    val currency: String,
    val status: String,
    val created_at: String?
)
