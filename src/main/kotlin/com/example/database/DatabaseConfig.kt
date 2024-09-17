package com.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.user.Users
import org.flywaydb.core.Flyway

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

        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .load()

        try {
            flyway.migrate()
            println("Migraciones ejecutadas correctamente")
        } catch (e: Exception) {
            println("Error al ejecutar migraciones: ${e.message}")
        }

        Database.connect(dataSource)
    }
}
