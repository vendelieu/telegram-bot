plugins {
    id("publish")
}

onlyJvmConfiguredKotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":telegram-bot"))
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.redis)
            }
        }
    }
}

libraryData {
    name = "Ktgram BotCtx Redis"
    description = "A collection of BotContext implementations designed to work with Redis."
}
