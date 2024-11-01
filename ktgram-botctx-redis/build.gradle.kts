plugins {
    id("publish")
}

onlyJvmConfiguredKotlin {
    mingwX64()
    linuxX64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":telegram-bot"))
            implementation(libs.kotlin.serialization)
            implementation(libs.redis)
        }
    }
}

libraryData {
    name = "Ktgram BotCtx Redis"
    description = "A collection of BotContext implementations designed to work with Redis."
}
