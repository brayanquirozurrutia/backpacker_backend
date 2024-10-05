package com.example.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthDate: String,
    val gender: String,
    val password: String,
    val confirmPassword: String
)

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

@Serializable
data class  ReSendActivationRequest(
    val email: String
)