package com.example.auth

import com.example.user.UserRepository

object AuthService {
    fun authenticate(email: String, password: String): Boolean {
        return UserRepository.checkPassword(email, password)
    }

    fun generateTokenForUser(email: String): String? {
        val user = UserRepository.findUserByEmail(email)
        return if (user != null) {
            JwtConfig.generateToken(user.id.toString())
        } else {
            null
        }
    }
}
