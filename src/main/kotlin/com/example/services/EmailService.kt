package com.example.services

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.InternalAPI
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object EmailService {
    private val dotenv = dotenv()

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }

    @Serializable
    data class EmailRequest(
        val sender: Sender,
        val to: List<Recipient>,
        val subject: String,
        val htmlContent: String
    )

    @Serializable
    data class Sender(val name: String, val email: String)

    @Serializable
    data class Recipient(val email: String, val name: String)

    @OptIn(InternalAPI::class)
    suspend fun sendEmailWithBrevoAPI(toEmail: String, toName: String, subject: String, htmlContent: String) {
        val apiKey = dotenv["BREVO_API_KEY"]
        val senderEmail = dotenv["BREVO_EMAIL"]

        val emailRequest = EmailRequest(
            sender = Sender("Backpacker", senderEmail),
            to = listOf(Recipient(toEmail, toName)),
            subject = subject,
            htmlContent = htmlContent
        )

        try {
            val emailRequestBody = Json.encodeToString(EmailRequest.serializer(), emailRequest)

            val response = client.post("https://api.brevo.com/v3/smtp/email") {
                contentType(ContentType.Application.Json)
                header("api-key", apiKey)
                body = emailRequestBody
            }

            println("Correo enviado correctamente: ${response.status}")
        } catch (e: Exception) {
            println("Error al enviar el correo: ${e.message}")
            e.printStackTrace()
        }
    }

}
