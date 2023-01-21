import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("jvm") version "1.8.0"
    `java-library`
    id("org.jetbrains.dokka") version "1.7.20"
    id("org.jmailen.kotlinter") version "3.13.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

val logbackVer: String by project
val jacksonVer: String by project
val ktorVer: String by project
val junitVer: String by project
val kotestVer: String by project

val javaTargetVersion = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVer)
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVer")

    implementation("io.ktor:ktor-client-core:$ktorVer")
    implementation("io.ktor:ktor-client-cio:$ktorVer")
    implementation("io.ktor:ktor-client-logging:$ktorVer")

    implementation(group = "org.reflections", name = "reflections", version = "0.10.2")
    implementation(kotlin("reflect"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    api(group = "ch.qos.logback", name = "logback-classic", version = logbackVer)

    testImplementation("ch.qos.logback:logback-classic:$logbackVer")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVer")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVer")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVer")
    testImplementation("io.ktor:ktor-client-mock:$ktorVer")
}

group = "eu.vendeli"
version = "2.5.4"

apply(from = "publishing.gradle.kts")

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config = files("$rootDir/detekt.yml")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = javaTargetVersion.majorVersion
        incremental = true
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = javaTargetVersion.majorVersion
    }

    test {
        useJUnitPlatform()
    }

    dokkaHtml.configure {
        outputDirectory.set(buildDir.resolve("dokka"))
    }

    withType<DokkaTask>().configureEach {
        dokkaSourceSets {
            named("main") {
                moduleName.set("Telegram Bot")
            }
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = javaTargetVersion
    targetCompatibility = javaTargetVersion
}
