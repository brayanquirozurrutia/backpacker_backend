package com.example.home

import com.example.plugins.UserIdKey
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*

fun Route.homeRoutes() {
    post("/") {
        val userId = call.attributes[UserIdKey]
        val response = HomeResponse(success = true, message = "Welcome to the home page!")
        call.respond(HttpStatusCode.OK, response)
    }
}
