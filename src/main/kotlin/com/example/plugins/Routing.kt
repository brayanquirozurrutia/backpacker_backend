package com.example.plugins

import com.example.auth.authRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRoutes()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
