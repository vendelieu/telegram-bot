import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    `maven-publish`
    `java-library`
    id("org.jetbrains.dokka") version "1.6.21"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
}

val logbackVer: String by project
val jacksonVer: String by project
val ktorVer: String by project

val javaTargetVersion = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(group = "ch.qos.logback", name = "logback-core", version = logbackVer)

    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVer)
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVer")

    implementation("io.ktor:ktor-client-cio:$ktorVer")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVer")
    implementation("io.ktor:ktor-client-logging:$ktorVer")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVer")

    implementation(group = "org.reflections", name = "reflections", version = "0.10.2")
    implementation(kotlin("reflect"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
}

group = "eu.vendeli"
version = "1.3.0"

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name, "Implementation-Version" to project.version))
    }
}

tasks.compileKotlin {
    incremental = true
    kotlinOptions.allWarningsAsErrors = true
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "telegram-bot"
            version = project.version.toString()

            from(components["kotlin"])
        }
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            moduleName.set("Telegram Bot")
        }
    }
}

java {
    withSourcesJar()
    sourceCompatibility = javaTargetVersion
    targetCompatibility = javaTargetVersion
}
