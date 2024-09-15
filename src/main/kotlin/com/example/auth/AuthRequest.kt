package com.example.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(val email: String, val password: String)