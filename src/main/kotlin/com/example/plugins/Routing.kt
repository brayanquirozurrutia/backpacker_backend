package com.example.plugins

import com.example.auth.authRoutes
import com.example.home.homeRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/auth") {
            authRoutes()
        }

        route("/home") {
            homeRoutes()
        }

        get("/") {
            call.respondText("Hello World!")
        }
    }
}
