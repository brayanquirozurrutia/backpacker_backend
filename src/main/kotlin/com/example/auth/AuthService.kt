package com.example.auth

import com.example.user.UserRepository

object AuthService {
    fun authenticate(email: String, password: String): Boolean {
        return UserRepository.checkPassword(email, password)
    }
}
