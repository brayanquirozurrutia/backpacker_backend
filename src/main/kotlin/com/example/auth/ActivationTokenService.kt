package com.example.auth

import com.example.user.ActivationTokens
import com.example.user.User
import com.example.user.Users
import com.example.user.toUser
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import kotlin.random.Random
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object ActivationTokenService {

    fun generateToken(userId: Int): String {
        var token: String = ""
        val expirationDate = LocalDateTime.now().plusHours(1)

        transaction {
            val existingToken = ActivationTokens.select { ActivationTokens.userId eq userId }.singleOrNull()

            if (existingToken != null) {
                ActivationTokens.deleteWhere { ActivationTokens.userId eq userId }
            }

            do {
                token = Random.Default.nextInt(100000, 999999).toString()
                val tokenExists = ActivationTokens.select { ActivationTokens.token eq token }.count() > 0
            } while (tokenExists)

            ActivationTokens.insert {
                it[this.userId] = userId
                it[this.token] = token
                it[this.expirationDate] = expirationDate
            }
        }
        return token
    }

    fun activateAccount(token: String): Boolean {
        return transaction {
            val validToken = ActivationTokens
                .select {
                    (ActivationTokens.token eq token) and
                            (ActivationTokens.expirationDate greaterEq LocalDateTime.now())
                }.singleOrNull()

            if (validToken != null) {
                val userId = validToken[ActivationTokens.userId]

                Users.update({ Users.id eq userId }) {
                    it[this.isActive] = true
                }

                ActivationTokens.deleteWhere { ActivationTokens.token eq token }

                true
            } else {
                false
            }
        }
    }

    fun getUserByToken(token: String): User? {
        return transaction {
            val userId = ActivationTokens.select { ActivationTokens.token eq token }
                .map { it[ActivationTokens.userId] }
                .singleOrNull()

            userId?.let {
                Users.select { Users.id eq it }
                    .map { row -> row.toUser() }
                    .singleOrNull()
            }
        }
    }
}