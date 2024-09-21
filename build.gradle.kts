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
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.0.0-rc-1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0-rc-1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")
    implementation("org.flywaydb:flyway-core:9.0.0")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.ktor:ktor-server-auth:3.0.0-rc-1")
    implementation("io.ktor:ktor-server-auth-jwt:3.0.0-rc-1")
    implementation("com.auth0:java-jwt:3.19.2")
}
