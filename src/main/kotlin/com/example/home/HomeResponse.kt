package com.example.home

import com.example.trip.TripData
import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val success: Boolean,
    val trips: List<TripData> = emptyList(),
    val message: String? = null,
)