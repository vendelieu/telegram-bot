plugins {
    dokka
    publish
}

onlyJvmConfiguredKotlin {
    sourceSets {
        jvmMain.dependencies {
            compileOnly(project(":telegram-bot"))
            compileOnly(libs.ktor.client.core)
            api(libs.ktor.server.core)
            api(libs.ktor.server.netty)
            implementation(libs.ayza)
        }
    }
}

libraryData {
    name = "Ktor starter"
    description = "Ktor webhook starter for KtGram."
}
