plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":api"))
    implementation(project(":domain"))
    implementation(project(":data"))
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("DatabaseCheckKt")
}