plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":api"))
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation("io.ktor:ktor-server-core:3.6.1")
    implementation("io.ktor:ktor-server-netty:3.6.1")

    implementation("org.slf4j:slf4j-api:2.0.19")
    runtimeOnly("ch.qos.logback:logback-classic:1.6.5")
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("bootstrap.MainKt")
    applicationName = "backend"
}