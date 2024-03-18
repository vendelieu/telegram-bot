plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("publish")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
        compilations.all {
            kotlinOptions.jvmTarget = JAVA_TARGET_V
        }
    }
    js { nodejs() }
    mingwX64()
    linuxX64()
    jvmToolchain(JAVA_TARGET_V_int)

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":telegram-bot"))
                implementation(libs.kotlin.datetime)
            }
        }
    }
}

libraryData {
    name.set("KtGram utils")
    description.set("KtGram library utilities.")
}
