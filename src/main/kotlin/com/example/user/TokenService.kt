package com.example.user

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import kotlin.random.Random
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object TokenService {

    fun generateToken(userId: Int, tokenType: TokenType): String {
        var token: String = ""
        val expirationDate = LocalDateTime.now().plusHours(1)

        transaction {
            val existingToken = TokenEntity.select {
                (TokenEntity.userId eq userId) and (TokenEntity.tokenType eq tokenType)
            }.singleOrNull()

            if (existingToken != null) {
                TokenEntity.deleteWhere {
                    (TokenEntity.userId eq userId) and (TokenEntity.tokenType eq tokenType)
                }
            }

            do {
                token = Random.Default.nextInt(100000, 999999).toString()
                val tokenExists = TokenEntity.select { TokenEntity.token eq token }.count() > 0
            } while (tokenExists)

            TokenEntity.insert {
                it[this.userId] = userId
                it[this.token] = token
                it[this.expirationDate] = expirationDate
                it[this.tokenType] = tokenType
            }
        }
        return token
    }

    fun activateAccount(token: String): Boolean {
        return transaction {
            val validToken = TokenEntity
                .select {
                    (TokenEntity.token eq token) and
                            (TokenEntity.tokenType eq TokenType.ACTIVATION) and
                            (TokenEntity.expirationDate greaterEq LocalDateTime.now())
                }.singleOrNull()

            if (validToken != null) {
                val userId = validToken[TokenEntity.userId]

                Users.update({ Users.id eq userId }) {
                    it[this.isActive] = true
                }

                TokenEntity.deleteWhere { TokenEntity.token eq token }

                true
            } else {
                false
            }
        }
    }

    fun validateToken(token: String, tokenType: TokenType): Boolean {
        return transaction {
            val validToken = TokenEntity
                .select {
                    (TokenEntity.token eq token) and
                            (TokenEntity.tokenType eq tokenType) and
                            (TokenEntity.expirationDate greaterEq LocalDateTime.now())
                }.singleOrNull()

            validToken != null
        }
    }

    fun deleteToken(token: String, tokenType: TokenType): Boolean {
        return transaction {
            val deletedRows = TokenEntity.deleteWhere {
                (TokenEntity.token eq token) and (TokenEntity.tokenType eq tokenType)
            }
            deletedRows > 0
        }
    }

    fun getUserByToken(token: String, tokenType: TokenType): User? {
        return transaction {
            val userId = TokenEntity.select {
                (TokenEntity.token eq token) and (TokenEntity.tokenType eq tokenType)
            }.map { it[TokenEntity.userId] }.singleOrNull()

            userId?.let {
                Users.select { Users.id eq it }
                    .map { row -> row.toUser() }
                    .singleOrNull()
            }
        }
    }
}