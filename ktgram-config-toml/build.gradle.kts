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
                implementation("com.akuleshov7:ktoml-core:0.5.1") {
                    exclude("org.jetbrains.kotlinx", "kotlinx-datetime")
                }
                implementation("com.akuleshov7:ktoml-file:0.5.1")
            }
        }
    }
}

libraryData {
    name = "KtGram Config Env"
    description = "ConfigLoader implementation which works with Environment."
}
