plugins {
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    id("publish")
}

libraryData {
    name = "KSP processor"
    description = "KSP plugin for Telegram-bot lib to collect actions."
}

onlyJvmConfiguredKotlin {
    sourceSets {
        jvmMain {
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

