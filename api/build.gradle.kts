plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":domain"))

    implementation("io.ktor:ktor-server-core:3.6.1")
    implementation("io.ktor:ktor-server-netty:3.6.1")

    implementation("io.ktor:ktor-server-call-logging:3.6.1")
    implementation("org.slf4j:slf4j-api:2.0.19")

    implementation("io.ktor:ktor-server-content-negotiation:3.6.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.6.1")

    implementation("io.ktor:ktor-server-status-pages:3.6.1")

    implementation("io.ktor:ktor-server-call-id:3.6.1")

    testImplementation(kotlin("test-junit5"))
    testImplementation("io.ktor:ktor-server-test-host:3.6.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}