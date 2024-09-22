package com.example

import com.example.database.DatabaseConfig
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val logger = LoggerFactory.getLogger("Application")
    logger.info("Application module started")

    DatabaseConfig.init()

    configureContentNegotiation()
    configureSecurity()
    configureRouting()
}
