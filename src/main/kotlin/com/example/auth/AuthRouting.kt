package com.example.auth

import com.example.user.Gender
import com.example.user.UserRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import java.time.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.authRoutes() {
    post("/login") {
        val loginRequest = call.receive<AuthRequest>()
        val isAuthenticated = AuthService.authenticate(loginRequest.email, loginRequest.password)

        val response = LoginResponse(
            success = isAuthenticated,
            message = if (isAuthenticated) "User authenticated successfully" else "Invalid email or password"
        )

        call.respond(HttpStatusCode.OK, response)
    }

    post("/register") {
        val registerRequest = call.receive<RegisterRequest>()
        val birthDate = LocalDate.parse(registerRequest.birthDate)
        val gender = Gender.valueOf(registerRequest.gender.uppercase())

        UserRepository.createUser(
            registerRequest.firstName,
            registerRequest.lastName,
            registerRequest.email,
            birthDate,
            gender,
            registerRequest.password
        )

        call.respond(HttpStatusCode.Created, "User registered successfully")
    }
}