plugins {
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    alias(libs.plugins.kotlin.serialization)
}

onlyJvmConfiguredKotlin {
    sourceSets {
        jvmMain.dependencies {
            implementation(libs.kotlin.serialization)
            implementation(libs.ksp)
            implementation(libs.poet)
            implementation(libs.poet.ksp)
            implementation(project(":telegram-bot"))
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.from(files("$rootDir/detekt.yml"))
}
