plugins {
    id("publish")
}

onlyJvmConfiguredKotlin {
    sourceSets {
        jvmMain.dependencies {
            compileOnly(project(":telegram-bot"))
            compileOnly(libs.ktor.client.core)
            compileOnly(libs.spring.starter)
        }
    }
}

libraryData {
    name = "Spring boot starter"
    description = "Spring boot starter for KtGram."
}
