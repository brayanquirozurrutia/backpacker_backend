package com.example.trip

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

object TripRepository {
    private const val EARTH_RADIUS_KM = 6371.0

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

    fun getTripsWithinRadius(lat: Double, lon: Double, radiusKm: Double): List<TripData> {
        return transaction {
            Trips.selectAll().mapNotNull { row ->
                val latitude = row[Trips.latitudeRequested]
                val longitude = row[Trips.longitudeRequested]

                val distance = acos(
                    sin(Math.toRadians(lat)) * sin(Math.toRadians(latitude)) +
                            cos(Math.toRadians(lat)) * cos(Math.toRadians(latitude)) *
                            cos(Math.toRadians(longitude) - Math.toRadians(lon))
                ) * EARTH_RADIUS_KM

                if (distance <= radiusKm) {
                    TripData(
                        id = row[Trips.id].value,
                        userId = row[Trips.userId].value,
                        destination = row[Trips.destination],
                        latitude = latitude,
                        longitude = longitude,
                        status = row[Trips.status]
                    )
                } else {
                    null
                }
            }
        }
    }

}

@Serializable
data class TripData(
    val id: Int,
    val userId: Int,
    val destination: String,
    val latitude: Double,
    val longitude: Double,
    val status: String
)