package com.example.user

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {
    fun createUser(email: String, password: String) {
        transaction {
            Users.insert {
                it[Users.email] = email
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