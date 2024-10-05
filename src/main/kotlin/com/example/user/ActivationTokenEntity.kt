package com.example.user

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object ActivationTokens : IntIdTable("activation_tokens") {
    val userId = reference("user_id", Users)
    val token = varchar("token", 6)
    val expirationDate = datetime("expiration_date")
}