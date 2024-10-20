package com.example.trip

import kotlinx.serialization.Serializable

@Serializable
data class CreateTripRequest(
    val userId: Int,
    val destination: String,
    val latitudeRequested: Double,
    val longitudeRequested: Double,
)