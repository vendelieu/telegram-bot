plugins {
    alias(libs.plugins.ktlinter)
    dokka
    publish
}

libraryData {
    name = "KtGram mock server"
    description = "Local Telegram Bot API mock built from the scrapped spec, for testing bots without the network."
}

// Logging backend for the standalone server and the tests only, a library must not force one on its users.
val serverLogging: Configuration by configurations.creating
dependencies {
    serverLogging(libs.logback)
}

onlyJvmConfiguredKotlin {
    sourceSets {
        jvmMain.dependencies {
            api(libs.coroutines.core)
            api(libs.kotlin.serialization)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.netty)
            implementation(libs.slf4j.api)
        }
        jvmTest.dependencies {
            implementation(project(":telegram-bot"))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.java)
            implementation(libs.test.kotest.junit5)
            implementation(libs.test.kotest.assertions)
            implementation(libs.test.coroutines)
            runtimeOnly(libs.logback)
        }
    }
}

// Single source of truth for the API spec: the scrapped file the library itself is generated from.
val apiSpecFile = rootDir.resolve("buildSrc/src/main/resources/api.json")

tasks {
    named<ProcessResources>("jvmProcessResources") {
        from(apiSpecFile)
    }
    withType<Test> {
        useJUnitPlatform()
    }
    // ./gradlew :ktgram-mock-server:runMockServer --args="--port 8081"
    register<JavaExec>("runMockServer") {
        group = "application"
        description = "Starts the Telegram Bot API mock server."
        classpath = files(named("jvmJar")) + configurations.getByName("jvmRuntimeClasspath") + serverLogging + files("src/server")
        mainClass.set("eu.vendeli.tgbot.mock.MainKt")
    }
}
