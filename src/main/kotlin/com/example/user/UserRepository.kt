package com.example.user

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

object UserRepository {
    fun createUser(
        firstName: String,
        lastName: String,
        email: String,
        birthDate: LocalDate,
        gender: Gender,
        password: String
    ) {
        transaction {
            Users.insert {
                it[Users.firstName] = firstName
                it[Users.lastName] = lastName
                it[Users.email] = email
                it[Users.birthDate] = birthDate
                it[Users.gender] = gender
                it[Users.password] = password
            }
        }
    }

    fun findUserByEmail(email: String): ResultRow? {
        return transaction {
            Users.select { Users.email eq email }.singleOrNull()
        }
    }
}