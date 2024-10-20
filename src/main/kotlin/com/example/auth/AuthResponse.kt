package com.example.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val success: Boolean,
    val message: String? = null
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String? = null,
    val isActive: Boolean? = null,
    val userId: Int? = null
)