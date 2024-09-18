package com.example.user

import org.mindrot.jbcrypt.BCrypt
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
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

        transaction {
            Users.insert {
                it[Users.firstName] = firstName
                it[Users.lastName] = lastName
                it[Users.email] = email
                it[Users.birthDate] = birthDate
                it[Users.gender] = gender
                it[Users.password] = hashedPassword
            }
        }
    }

    fun findUserByEmail(email: String): ResultRow? {
        return transaction {
            Users.select { Users.email eq email }.singleOrNull()
        }
    }

    fun checkPassword(email: String, password: String): Boolean {
        val user = findUserByEmail(email) ?: return false
        val hashedPassword = user[Users.password]
        return BCrypt.checkpw(password, hashedPassword)
    }
}