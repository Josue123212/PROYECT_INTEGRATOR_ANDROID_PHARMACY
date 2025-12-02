package com.example.farmacia_medicitas.data.auth

import com.example.farmacia_medicitas.data.remote.ApiService
import com.example.farmacia_medicitas.data.remote.dto.auth.LoginRequest
import com.example.farmacia_medicitas.data.remote.dto.auth.LoginResponse
import com.example.farmacia_medicitas.data.remote.dto.auth.LoginEnvelope
import com.example.farmacia_medicitas.data.remote.dto.auth.UserDto
import com.example.farmacia_medicitas.data.remote.dto.auth.MeEnvelope
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(emailOrUsername: String, password: String): LoginResponse {
        val envelope: LoginEnvelope = api.login(LoginRequest(email_or_username = emailOrUsername, password = password))
        val res: LoginResponse = envelope.data
        tokenManager.saveTokens(res.access, res.refresh)
        return res
    }

    suspend fun register(email: String, username: String, password: String): UserDto {
        val env = api.register(com.example.farmacia_medicitas.data.remote.dto.auth.RegisterRequest(email, username, password))
        // Tras registrar, iniciar sesión automáticamente para obtener tokens
        runCatching {
            login(email, password)
        }
        return env.data
    }

    suspend fun logout() {
        tokenManager.clear()
    }

    fun getAccess(): String? = tokenManager.getAccessToken()
    fun getRefresh(): String? = tokenManager.getRefreshToken()

    // Protected call: fetch current user
    suspend fun me(): UserDto {
        val env: MeEnvelope = api.me()
        return env.user
    }

    // For tests (debug): invalidate access to force 401
    fun invalidateAccessForTest() {
        tokenManager.setAccessTokenForTest("invalid")
    }
}
