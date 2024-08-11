plugins {
    id("publish")
}

configuredKotlin {
    sourceSets {
        commonMain {
            dependencies {
                compileOnly(libs.coroutines.core)
                implementation(libs.redisKM)
                implementation(project(":telegram-bot"))
            }
        }
    }
}

libraryData {
    name.set("Ktgram BotCtx Redis")
    description.set("A collection of BotContext implementations designed to work with Redis.")
}
