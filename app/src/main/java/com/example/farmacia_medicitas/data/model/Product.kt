package com.example.farmacia_medicitas.data.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val basePrice: Double? = null,
    val salePrice: Double? = null,
    val saleActive: Boolean = false,
    val imageUrl: String? = null,
    val inStock: Boolean = true
)
