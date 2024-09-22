package com.example.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val success: Boolean,
    val message: String? = null,
)