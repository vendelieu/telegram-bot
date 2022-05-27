plugins {
    application
    kotlin("jvm") version "1.6.21"
}

group = "com.github.vendelieu.samples"
version = "0.0.1"
application {
    mainClass.set("com.github.vendelieu.samples.PollApplicationKt")
}

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation("com.github.vendelieu:telegram-bot:1.0.0")

    implementation("ch.qos.logback:logback-classic:1.2.11")
}