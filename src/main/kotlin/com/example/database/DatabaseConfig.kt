package com.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.user.Users

object DatabaseConfig {
    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://127.0.0.1:5433/backpacker"
            username = "myuser"
            password = "mypassword"
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        try {
            transaction {
                SchemaUtils.create(Users)
                println("Tables created successfully")
            }
        } catch (e: Exception) {
            println("Error creating tables: ${e.message}")
        }
    }
}
