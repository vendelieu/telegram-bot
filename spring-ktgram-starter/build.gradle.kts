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
                compileOnly(libs.spring.starter)
            }
        }
    }
}

libraryData {
    name.set("Spring boot starter")
    description.set("Spring boot starter for KtGram.")
}
