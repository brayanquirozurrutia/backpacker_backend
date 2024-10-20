package com.example.trip

import com.example.user.Users
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

enum class TripStatus {
    REQUESTED, IN_PROGRESS, COMPLETED
}

object Trips : IntIdTable("trip") {
    val userId = reference("user_id", Users)
    val destination = varchar("destination", 255)
    val latitudeRequested = double("latitude_requested")
    val longitudeRequested = double("longitude_requested")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
    val status = varchar("status", 20).default("REQUESTED")
}