package com.example.trip

import com.example.auth.AuthResponse
import com.example.user.UserRepository
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Route.tripRoutes() {
    post("/create") {
        val tripRequest = call.receive<CreateTripRequest>()

        val user = UserRepository.findUserById(tripRequest.userId)
        if (user == null || !user.isActive) {
            call.respond(HttpStatusCode.NotFound, AuthResponse(false, "Usuario no encontrado"))
            return@post
        }

        TripRepository.createTrip(
            userId = tripRequest.userId,
            destination = tripRequest.destination,
            latitudeRequested = tripRequest.latitudeRequested,
            longitudeRequested = tripRequest.longitudeRequested
        )

        call.respond(HttpStatusCode.Created, AuthResponse(true, "Viaje creado con éxito"))
    }
}
