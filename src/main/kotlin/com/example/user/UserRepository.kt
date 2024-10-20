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
    ): Int {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        var userId: Int = -1

        transaction {
            userId = Users.insertAndGetId {
                it[Users.firstName] = firstName
                it[Users.lastName] = lastName
                it[Users.email] = email
                it[Users.birthDate] = birthDate
                it[Users.gender] = gender
                it[Users.password] = hashedPassword
            }.value
        }
        return userId
    }

    fun findUserByEmail(email: String): User? {
        return transaction {
            Users.select { Users.email eq email }
                .map { it.toUser() }
                .singleOrNull()
        }
    }

    fun findUserById(userId: Int): User? {
        return transaction {
            Users.select { Users.id eq userId }
                .map { it.toUser() }
                .singleOrNull()
        }
    }

    fun checkPassword(email: String, password: String): Boolean {
        val user = findUserByEmail(email) ?: return false
        val hashedPassword = user.password
        return BCrypt.checkpw(password, hashedPassword)
    }

    fun updatePassword(email: String, newPassword: String) {
        val hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())

        transaction {
            Users.update({ Users.email eq email }) {
                it[password] = hashedPassword
            }
        }
    }
}