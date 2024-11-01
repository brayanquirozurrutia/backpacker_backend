package com.example.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripResponse(
    val id: Int,
    val userId: Int,
    val destination: String,
    val latitude: Double,
    val longitude: Double,
    val status: String
)