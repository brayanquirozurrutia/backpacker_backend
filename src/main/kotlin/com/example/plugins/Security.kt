package com.example.plugins

import com.example.auth.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.request.uri
import io.ktor.server.response.respondText
import io.ktor.util.*

val protectedRoutes = setOf(
    "/home",
    "/trip"
)

fun Application.configureSecurity() {
    intercept(ApplicationCallPipeline.Plugins) {
        val path = call.request.uri

        if (protectedRoutes.any { path.startsWith(it) }) {
            val authHeader = call.request.headers["Authorization"]
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                call.respondText("Token no proporcionado", status = HttpStatusCode.Unauthorized)
                finish()
            } else {
                val token = authHeader.removePrefix("Bearer ")
                try {
                    val decodedJWT = JwtConfig.verifyToken(token)
                    val userId = decodedJWT.getClaim("userId").asString()
                    call.attributes.put(UserIdKey, userId)
                } catch (e: Exception) {
                    call.respondText("Token inválido", status = HttpStatusCode.Unauthorized)
                    finish()
                }
            }
        }
    }
}

val UserIdKey = AttributeKey<String>("userId")
