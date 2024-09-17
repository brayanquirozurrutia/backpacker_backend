package com.example.user

import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.dao.id.IntIdTable

enum class Gender {
    MALE, FEMALE, OTHER
}

object Users : IntIdTable() {
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 255).uniqueIndex()
    val birthDate = date("birth_date")
    val gender = enumerationByName("gender", 10, Gender::class)
    val password = varchar("password", 64)
}