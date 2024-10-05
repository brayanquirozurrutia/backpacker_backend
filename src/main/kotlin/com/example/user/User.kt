package com.example.user

import java.time.LocalDate
import org.jetbrains.exposed.sql.ResultRow

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthDate: LocalDate,
    val gender: Gender,
    val password: String,
    val isActive: Boolean
)

fun ResultRow.toUser(): User {
    return User(
        id = this[Users.id].value,
        firstName = this[Users.firstName],
        lastName = this[Users.lastName],
        email = this[Users.email],
        birthDate = this[Users.birthDate],
        gender = this[Users.gender],
        password = this[Users.password],
        isActive = this[Users.isActive]
    )
}