package com.example.auth

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Route.authRoutes() {
    post("/login") {
        val loginRequest = call.receive<AuthRequest>()
        val isAuthenticated = AuthService.authenticate(loginRequest.email, loginRequest.password)

        if (isAuthenticated) {
            call.respond(HttpStatusCode.OK, "Login successful")
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Invalid email or password")
        }
    }
}