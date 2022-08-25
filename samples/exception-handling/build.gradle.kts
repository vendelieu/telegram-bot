plugins {
    application
    kotlin("jvm") version "1.6.21"
}

group = "eu.vendeli.samples"
version = "0.0.1"
application {
    mainClass.set("eu.vendeli.samples.ErrorHandlingApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("eu.vendeli:telegram-bot:2.1.0")
    implementation("ch.qos.logback:logback-classic:1.2.11")
}