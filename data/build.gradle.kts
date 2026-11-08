plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":domain"))
    implementation("com.zaxxer:HikariCP:7.0.2")

    runtimeOnly("org.postgresql:postgresql:42.7.14")

    testImplementation(kotlin("test"))

    implementation("org.slf4j:slf4j-api:2.0.19")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")

    implementation("org.flywaydb:flyway-core:13.10.0")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:13.10.0")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}