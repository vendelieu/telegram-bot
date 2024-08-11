plugins {
    id("publish")
}

configuredKotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":telegram-bot"))
                implementation(libs.env)
            }
        }
    }
}

libraryData {
    name = "KtGram Config Env"
    description = "ConfigLoader implementation which works with Environment."
}
