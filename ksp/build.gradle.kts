plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    id("publish")
}

libraryData {
    name = "KSP processor"
    description = "KSP plugin for Telegram-bot lib to collect actions."
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
    jvmToolchain(JAVA_TARGET_V_int)

    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(libs.ksp)
                implementation(libs.poet)
                implementation(libs.poet.ksp)
                implementation(project(":telegram-bot"))
            }
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.from(files("$rootDir/detekt.yml"))
}
