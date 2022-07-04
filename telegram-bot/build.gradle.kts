import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    `maven-publish`
    `java-library`
    id("org.jetbrains.dokka") version "1.6.21"
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
    compileOnly(group = "ch.qos.logback", name = "logback-classic", version = logbackVer)

    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVer)
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVer")

    implementation("io.ktor:ktor-client-cio:$ktorVer")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVer")
    implementation("io.ktor:ktor-client-logging:$ktorVer")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVer")

    implementation(group = "org.reflections", name = "reflections", version = "0.10.2")
    implementation(kotlin("reflect"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")

    testImplementation("ch.qos.logback:logback-classic:1.2.11")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVer")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVer")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVer")
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVer")
}

group = "eu.vendeli"
version = "1.5.0"

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

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name, "Implementation-Version" to project.version))
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
