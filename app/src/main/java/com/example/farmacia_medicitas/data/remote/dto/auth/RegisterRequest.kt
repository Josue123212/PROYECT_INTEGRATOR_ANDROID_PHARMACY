package com.example.farmacia_medicitas.data.remote.dto.auth

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)
