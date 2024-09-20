package com.example.auth

import com.example.user.Gender
import com.example.user.UserRepository
import com.example.utils.convertDateFormatIfNecessary
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun Route.authRoutes() {
    post("/login") {
        val loginRequest = call.receive<AuthRequest>()
        val isAuthenticated = AuthService.authenticate(loginRequest.email, loginRequest.password)

        val response = AuthResponse(
            success = isAuthenticated,
            message = if (isAuthenticated) "User authenticated successfully" else "Invalid email or password"
        )

        call.respond(HttpStatusCode.OK, response)
    }

    post("/register") {
        val registerRequest = call.receive<RegisterRequest>()

        val cleanedFirstName = registerRequest.firstName.trim().uppercase()
        val cleanedLastName = registerRequest.lastName.trim().uppercase()
        val cleanedEmail = registerRequest.email.trim()
        val cleanedBirthDate = registerRequest.birthDate.trim()
        val cleanedGender = registerRequest.gender.trim().uppercase()
        val password = registerRequest.password
        val confirmPassword = registerRequest.confirmPassword

        if (password != confirmPassword) {
            call.respond(
                HttpStatusCode.BadRequest,
                AuthResponse(success = false, message = "Las contraseñas no coinciden")
            )
            return@post
        }

        val formattedBirthDate = try {
            val formattedDate = convertDateFormatIfNecessary(cleanedBirthDate)
            LocalDate.parse(formattedDate)
        } catch (e: DateTimeParseException) {
            call.respond(
                HttpStatusCode.BadRequest,
                AuthResponse(success = false, message = "Formato de fecha no válido")
            )
            return@post
        }

        if (LocalDate.now().year - formattedBirthDate.year < 18) {
            call.respond(
                HttpStatusCode.BadRequest,
                AuthResponse(success = false, message = "La edad mínima es de 18 años")
            )
            return@post
        }

        val gender = try {
            Gender.valueOf(cleanedGender)
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                AuthResponse(success = false, message = "Género no válido")
            )
            return@post
        }

        val existingUser = UserRepository.findUserByEmail(registerRequest.email)

        if (existingUser != null) {
            call.respond(
                HttpStatusCode.Conflict,
                AuthResponse(success = false, message = "El correo ya está en uso")
            )
            return@post
        }

        try {
            UserRepository.createUser(
                cleanedFirstName,
                cleanedLastName,
                cleanedEmail,
                formattedBirthDate,
                gender,
                password
            )
            call.respond(
                HttpStatusCode.Created,
                AuthResponse(success = true, message = "Usuario registrado exitosamente")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                AuthResponse(success = false, message = "Ocurrió un error al registrar el usuario")
            )
        }
    }

    post("/forgot-password") {
        val request = call.receive<ForgotPasswordRequest>()
        val user = UserRepository.findUserByEmail(request.email)

        if (user == null) {
            call.respond(
                HttpStatusCode.NotFound,
                AuthResponse(success = false, message = "El correo no está registrado")
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                AuthResponse(success = true, message = "El correo está registrado")
            )
        }
    }

    post("/reset-password") {
        val resetRequest = call.receive<ResetPasswordRequest>()

        val email = resetRequest.email.trim()
        val newPassword = resetRequest.password
        val confirmPassword = resetRequest.confirmPassword

        val user = UserRepository.findUserByEmail(email)
        if (user == null) {
            call.respond(
                HttpStatusCode.NotFound,
                AuthResponse(success = false, message = "El correo electrónico no está registrado")
            )
            return@post
        }

        if (newPassword != confirmPassword) {
            call.respond(
                HttpStatusCode.BadRequest,
                AuthResponse(success = false, message = "Las contraseñas no coinciden")
            )
            return@post
        }

        try {
            UserRepository.updatePassword(email, newPassword)
            call.respond(
                HttpStatusCode.OK,
                AuthResponse(success = true, message = "Contraseña actualizada exitosamente")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                AuthResponse(success = false, message = "Ocurrió un error al actualizar la contraseña")
            )
        }
    }
}