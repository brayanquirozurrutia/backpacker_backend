package com.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.flywaydb.core.Flyway
import io.github.cdimascio.dotenv.dotenv

object DatabaseConfig {
    private val dotenv = dotenv()

    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = dotenv["DATABASE_URL"]
            username = dotenv["DATABASE_USER"]
            password = dotenv["DATABASE_PASSWORD"]
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
