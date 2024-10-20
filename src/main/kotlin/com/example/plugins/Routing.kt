package com.example.plugins

import com.example.auth.authRoutes
import com.example.home.homeRoutes
import com.example.trip.tripRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/auth") {
            authRoutes()
        }

        route("/home") {
            homeRoutes()
        }

        route("/trip") {
            tripRoutes()
        }
    }
}
