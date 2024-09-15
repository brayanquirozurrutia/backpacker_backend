package com.example.user

import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable() {
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 64)  // Encriptarás la contraseña antes de guardarla
}