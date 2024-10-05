val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "3.0.0-rc-1"
    kotlin("plugin.serialization") version "2.0.20"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor server dependencies (actualizado a versiones estables)
    implementation("io.ktor:ktor-server-core-jvm:2.3.0")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.0")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.0")
    implementation("io.ktor:ktor-server-auth:2.3.0")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.0")

    // Ktor client dependencies
    implementation("io.ktor:ktor-client-core:2.3.0")
    implementation("io.ktor:ktor-client-cio:2.3.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.12")

    // Database (Exposed y PostgreSQL)
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("com.zaxxer:HikariCP:4.0.3")

    // Flyway for database migrations
    implementation("org.flywaydb:flyway-core:9.0.0")

    // BCrypt for password hashing
    implementation("org.mindrot:jbcrypt:0.4")

    // JWT Token management
    implementation("com.auth0:java-jwt:3.19.2")

    // Java Mail (for sending emails)
    implementation("com.sun.mail:javax.mail:1.6.2")

    // dotenv for environment variables
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // Test dependencies
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
}