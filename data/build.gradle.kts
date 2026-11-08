plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":domain"))

    runtimeOnly("org.postgresql:postgresql:42.7.14")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}