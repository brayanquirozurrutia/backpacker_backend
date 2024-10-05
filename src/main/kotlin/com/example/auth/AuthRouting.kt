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
import com.example.services.EmailService

fun Route.authRoutes() {
    post("/login") {
        val loginRequest = call.receive<AuthRequest>()
        val isAuthenticated = AuthService.authenticate(loginRequest.email, loginRequest.password)

        val token = if (isAuthenticated) {
            AuthService.generateTokenForUser(loginRequest.email)
        } else {
            null
        }

        val response = if (token != null) {
            LoginResponse(
                success = true,
                message = "User authenticated successfully",
                token = token
            )
        } else {
            LoginResponse(
                success = false,
                message = "Invalid email or password",
                token = null
            )
        }

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
            val userId = UserRepository.createUser(
                cleanedFirstName,
                cleanedLastName,
                cleanedEmail,
                formattedBirthDate,
                gender,
                password
            )

            val activationToken = ActivationTokenService.generateToken(userId)

            EmailService.sendEmailWithBrevoAPI(
                toEmail = cleanedEmail,
                toName = cleanedFirstName,
                subject = "Cuenta creada exitosamente",
                htmlContent = """
                    <h1>Hola $cleanedFirstName,</h1>
                    
                    <p>Tu código de activación de cuenta es: <strong>$activationToken</strong>.</p>
                    """.trimIndent()
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

    post("/activate-account/{token}") {
        val token = call.parameters["token"]

        if (token.isNullOrBlank()) {
            call.respond(
                HttpStatusCode.BadRequest,
                AuthResponse(success = false, message = "Token inválido")
            )
            return@post
        }

        val user = ActivationTokenService.getUserByToken(token)

        if (user == null) {
            call.respond(
                HttpStatusCode.InternalServerError,
                AuthResponse(success = false, message = "Ocurrió un error al activar la cuenta")
            )
            return@post
        }

        val activationSuccess = ActivationTokenService.activateAccount(token)

        if (activationSuccess) {

            var userName = user.firstName
            val userEmail = user.email

            EmailService.sendEmailWithBrevoAPI(
                toEmail = userEmail,
                toName = userName,
                subject = "Cuenta activada exitosamente",
                htmlContent = """
                    <h1>Cuenta activada</h1>
                    
                    <p>Tu cuenta ha sido activada exitosamente.</p>
                    """.trimIndent()
            )

            call.respond(
                HttpStatusCode.OK,
                AuthResponse(success = true, message = "Cuenta activada exitosamente")
            )
        } else {
            call.respond(
                HttpStatusCode.BadRequest,
                AuthResponse(success = false, message = "Token inválido o expirado")
            )
        }
    }

    post("/resend-activation-code") {
        val resendRequest = call.receive<ReSendActivationRequest>()
        val email = resendRequest.email.trim()
        val user = UserRepository.findUserByEmail(email)

        if (user == null) {
            call.respond(
                HttpStatusCode.NotFound,
                AuthResponse(success = false, message = "Correo no válido")
            )
        } else {
            val userId = user.id
            val activationToken = ActivationTokenService.generateToken(userId)

            EmailService.sendEmailWithBrevoAPI(
                toEmail = email,
                toName = user.firstName,
                subject = "Código de activación",
                htmlContent = """
                    <h1>Hola ${user.firstName},</h1>
                    
                    <p>Tu código de activación de cuenta es: <strong>$activationToken</strong>.</p>
                    """.trimIndent()
            )

            call.respond(
                HttpStatusCode.OK,
                AuthResponse(success = true, message = "Código de activación reenviado exitosamente")
            )
        }
    }

    post("/forgot-password") {
        val request = call.receive<ForgotPasswordRequest>()
        val user = UserRepository.findUserByEmail(request.email)

        if (user == null) {
            call.respond(
                HttpStatusCode.NotFound,
                AuthResponse(success = false, message = "Correo no válido")
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                AuthResponse(success = true, message = "ok")
            )
        }
    }

    post("/reset-password") {
        val resetRequest = call.receive<ResetPasswordRequest>()

        val email = resetRequest.email.trim()
        val newPassword = resetRequest.password.trim()
        val confirmPassword = resetRequest.confirmPassword.trim()

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