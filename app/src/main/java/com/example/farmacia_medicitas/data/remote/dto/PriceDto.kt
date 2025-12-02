package com.example.farmacia_medicitas.data.remote.dto

data class PriceDto(
    val currency: String,
    val amount: Double,
    val sale_amount: Double?,
    val sale_active: Boolean?,
    val sale_ends_at: String?
)
