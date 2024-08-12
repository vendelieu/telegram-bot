plugins {
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
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

