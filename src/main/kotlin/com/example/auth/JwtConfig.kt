package com.example.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
    private const val secret = "your_secret_key"
    private const val issuer = "com.example.backpaker"
    private const val validityInMs = 36_000_00 * 10
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(userId: String): String = JWT.create()
        .withIssuer(issuer)
        .withClaim("userId", userId)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    fun verifyToken(token: String) = JWT.require(algorithm).withIssuer(issuer).build().verify(token)

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}
