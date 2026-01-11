plugins {
    dokka
    publish
}

onlyJvmConfiguredKotlin {
    mingwX64()
    linuxX64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":telegram-bot"))
            implementation(libs.toml)
        }
    }
}

libraryData {
    name = "KtGram Config Toml"
    description = "ConfigLoader implementation which works with Toml."
}
