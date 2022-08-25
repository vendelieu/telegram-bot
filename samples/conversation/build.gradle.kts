plugins {
    application
    kotlin("jvm") version "1.6.21"
}

group = "eu.vendeli.samples"
version = "0.0.1"
application {
    mainClass.set("eu.vendeli.samples.EchoApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("eu.vendeli:telegram-bot:2.1.0")

    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation(group = "org.redisson", name = "redisson", version = "3.17.0") {
        exclude("com.fasterxml.jackson.core", "jackson-databind")
    }
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
}