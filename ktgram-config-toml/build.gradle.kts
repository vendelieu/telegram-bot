plugins {
    id("publish")
}

onlyJvmConfiguredKotlin {
    mingwX64()
    linuxX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":telegram-bot"))
                implementation(libs.kotlin.datetime)
                implementation(libs.ktoml.core)
                implementation(libs.ktoml.file)
            }
        }
    }
}

libraryData {
    name = "KtGram Config Env"
    description = "ConfigLoader implementation which works with Environment."
}
