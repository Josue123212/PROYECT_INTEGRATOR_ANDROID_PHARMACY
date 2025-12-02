package com.example.farmacia_medicitas.data.mappers

import com.example.farmacia_medicitas.data.model.Category
import com.example.farmacia_medicitas.data.remote.dto.CategoryDto

fun CategoryDto.toDomain(): Category = Category(
    id = id,
    name = name,
    slug = slug
)
