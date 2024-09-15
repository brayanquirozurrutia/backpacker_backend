package com.example.auth

import com.example.user.UserRepository
import com.example.user.Users

object AuthService {
    fun authenticate(email: String, password: String): Boolean {
        val user = UserRepository.findUserByEmail(email)
        return user != null && user[Users.password] == password
    }
}