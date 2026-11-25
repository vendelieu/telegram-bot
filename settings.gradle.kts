plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "ktgram"

include(":telegram-bot")
include(":api-sentinel")
include(":docs")
include(":ktnip")
include(":webapps")
include(":ktgram-utils")
include(":ktgram-config-toml")
include(":ktgram-gradle-plugin")
include(":ktor-starter")
include(":spring-ktgram-starter")
include(":quarkus-ktgram")
include(":quarkus-ktgram-deployment")
project(":quarkus-ktgram").projectDir = file("quarkus-extension/runtime")
project(":quarkus-ktgram-deployment").projectDir = file("quarkus-extension/deployment")
