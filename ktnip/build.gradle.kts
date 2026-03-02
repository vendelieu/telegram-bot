plugins {
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    alias(libs.plugins.ksp)
    id("publish")
}

libraryData {
    name = "KSP processor"
    description = "KSP plugin for Telegram-bot lib to collect required context."
}

onlyJvmConfiguredKotlin {
    sourceSets {
        jvmMain.dependencies {
            implementation(libs.ksp)
            implementation(libs.poet)
            implementation(libs.poet.ksp)
            implementation(project(":telegram-bot"))
            implementation(libs.autoService.annotations)
        }

        jvmTest.dependencies {
            implementation(project(":telegram-bot"))
            implementation(libs.test.compile.ksp)
            implementation(libs.test.kotest.junit5)
            implementation(libs.test.kotest.assertions)
        }
    }
}

dependencies {
    add("kspJvm", libs.autoService.ksp)
}

tasks.withType<Test> { useJUnitPlatform() }

val goldenOutputDir = layout.projectDirectory.dir("src/jvmTest/resources/test-data/golden")

tasks.register<Test>("updateGolden") {
    group = "verification"
    description = "Updates golden files for KSP processor tests. Run when generated output changes intentionally."
    testClassesDirs = sourceSets["jvmTest"].output.classesDirs
    classpath = sourceSets["jvmTest"].runtimeClasspath
    useJUnitPlatform()
    systemProperty("ktnip.golden.update", "true")
    systemProperty("ktnip.golden.outputDir", goldenOutputDir.asFile.absolutePath)
    outputs.upToDateWhen { false }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.from(files("$rootDir/detekt.yml"))
}
