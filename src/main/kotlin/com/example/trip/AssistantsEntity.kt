package com.example.trip

import com.example.user.Users
import org.jetbrains.exposed.dao.id.IntIdTable

object Assistants : IntIdTable("assistant") {
    val tripId = reference("trip_id", Trips)
    val userId = reference("user_id", Users)
    init {
        uniqueIndex(tripId, userId)
    }
}