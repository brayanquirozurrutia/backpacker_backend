package com.example.trip

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object TripRepository {

    fun createTrip(
        userId: Int,
        destination: String,
        latitudeRequested: Double,
        longitudeRequested: Double
    ): Int {
        return transaction {
            Trips.insertAndGetId {
                it[Trips.userId] = userId
                it[Trips.destination] = destination
                it[Trips.latitudeRequested] = latitudeRequested
                it[Trips.longitudeRequested] = longitudeRequested
            }.value
        }
    }

    fun updateTripStatus(tripId: Int, newStatus: TripStatus) {
        transaction {
            Trips.update({ Trips.id eq tripId }) {
                it[status] = newStatus.name
            }
        }
    }
}
