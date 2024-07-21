plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("publish")
}

kotlin {
    jvmToolchain(JAVA_TARGET_V_int)
    jvm()
    sourceSets {
        jvmMain {
            dependencies {
                compileOnly(project(":telegram-bot"))
                compileOnly(libs.ktor.client.core)
                api(libs.ktor.server.core)
                api(libs.ktor.server.netty)
                implementation(libs.ssl.utils)
            }
        }
    }
}

libraryData {
    name.set("Ktor starter")
    description.set("Ktor webhook starter for KtGram.")
}
