import org.gradle.api.publish.PublishingExtension

val javaTargetVersion = JavaVersion.VERSION_11

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    `maven-publish`
    signing
}

group = "eu.vendeli"
version = providers.gradleProperty("libVersion").getOrElse("dev")
description = "KSP plugin for Telegram-bot lib to collect actions."

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ksp)
    implementation(libs.poet)
    implementation(libs.poet.ksp)
    implementation(project(":telegram-bot"))
}

val sonatypeUsername = (project.findProperty("sonatypeUsername") as String?) ?: ""
val sonatypePassword = (project.findProperty("sonatypePassword") as String?) ?: ""

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            pom {
                name.set(artifactId)
                description.set(project.description)
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
                    username = sonatypeUsername
                    password = sonatypePassword
                }
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
        }
        configure<SigningExtension> {
            if(sonatypeUsername.isNotEmpty() && sonatypePassword.isNotEmpty()) {
                sign((extensions["publishing"] as PublishingExtension).publications["maven"])
            }
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.from(files("$rootDir/detekt.yml"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = javaTargetVersion.majorVersion
        kotlinOptions.allWarningsAsErrors = true
        incremental = true
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = javaTargetVersion.majorVersion
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = javaTargetVersion
    targetCompatibility = javaTargetVersion
}
