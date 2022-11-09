import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    `maven-publish`
    `java-library`
    id("org.jetbrains.dokka") version "1.7.20"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    signing
}

val logbackVer: String by project
val jacksonVer: String by project
val ktorVer: String by project
val junitVer: String by project

val javaTargetVersion = JavaVersion.VERSION_1_8

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
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVer")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVer")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVer")
    testImplementation("io.ktor:ktor-client-mock:$ktorVer")
}

group = "eu.vendeli"
version = "2.2.2"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "telegram-bot"
            version = project.version.toString()

            pom {
                name.set(artifactId)
                description.set("Telegram Bot API wrapper, with handy Kotlin DSL.")
                url.set("https://github.com/vendelieu/telegram-bot")
                licenses {
                    license {
                        name.set("Apache 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("Vendelieu")
                        name.set("Vendelieu")
                        email.set("vendelieu@gmail.com")
                        url.set("https://vendeli.eu")
                    }
                }
                scm {
                    connection.set("scm:git:github.com/vendelieu/telegram-bot.git")
                    developerConnection.set("scm:git:ssh://github.com/vendelieu/telegram-bot.git")
                    url.set("https://github.com/vendelieu/telegram-bot.git")
                }
            }

            from(components["java"])
        }

        repositories {
            maven {
                credentials {
                    username = System.getenv()["SONATYPE_USERNAME"]
                    password = System.getenv()["SONATYPE_PASSWORD"]
                }
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
        }
        signing {
            sign(publishing.publications["maven"])
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

tasks.compileKotlin {
    incremental = true
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = javaTargetVersion
    targetCompatibility = javaTargetVersion
}
