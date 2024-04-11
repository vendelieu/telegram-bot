plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("publish")
}

kotlin {
    jvmToolchain(JAVA_TARGET_V_int)
    jvm()
    sourceSets {
        named("jvmMain") {
            dependencies {
                compileOnly(project(":telegram-bot"))
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.netty)
                implementation(libs.ssl.utils)
            }
        }
    }
}

libraryData {
    name.set("Ktor starter")
    description.set("KtGram ktor webhook starter.")
}
