package com.example.home

import com.example.trip.TripRepository
import com.example.trip.TripsRequest
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.request.receive

fun Route.homeRoutes() {
    post("/") {
        val tripsRequest = call.receive<TripsRequest>()

        val trips = TripRepository.getTripsWithinRadius(
            lat = tripsRequest.lat,
            lon = tripsRequest.lon,
            radiusKm = tripsRequest.radiusKm
        )

        val response = HomeResponse(success = true, trips = trips)
        call.respond(HttpStatusCode.OK, response)
    }

    post("/trips") {
        val tripsRequest = call.receive<TripsRequest>()

        val trips = TripRepository.getTripsWithinRadius(
            lat = tripsRequest.lat,
            lon = tripsRequest.lon,
            radiusKm = tripsRequest.radiusKm
        )

        call.respond(HttpStatusCode.OK, trips)
    }
}
