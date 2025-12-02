package com.example.farmacia_medicitas.data.repository

import com.example.farmacia_medicitas.core.Result
import com.example.farmacia_medicitas.data.model.Category
import com.example.farmacia_medicitas.data.mappers.toDomain
import com.example.farmacia_medicitas.data.remote.ApiService
import javax.inject.Inject

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
}

class RemoteCategoryRepository @Inject constructor(
    private val api: ApiService
) : CategoryRepository {
    override suspend fun getCategories(): Result<List<Category>> = try {
        val resp = api.getCategories()
        val list = resp.results.map { it.toDomain() }
        Result.Success(list)
    } catch (e: Exception) {
        Result.Error(e.localizedMessage ?: "Error cargando categorías", e)
    }
}
