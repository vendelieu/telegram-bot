plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "ktgram"

include(":telegram-bot")
include(":api-sentinel")
include(":docs")
include(":ksp")
include(":webapps")
include(":ktgram-utils")
include(":ktgram-botctx-redis")
include(":ktgram-config-toml")
include(":ktgram-gradle-plugin")
include(":ktor-starter")
include(":spring-ktgram-starter")
include(":aide")
