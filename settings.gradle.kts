plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "ktgram"

include(":telegram-bot")
include(":helper")
include(":ksp")
include(":webapps")
include(":ktgram-utils")
include(":ktgram-botctx-redis")
include(":ktgram-config-env")
include(":ktgram-config-toml")
include(":ktgram-gradle-plugin")
include(":ktor-starter")
include(":spring-ktgram-starter")
