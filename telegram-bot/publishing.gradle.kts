val sonatypeUsername = (project.findProperty("sonatypeUsername") as String?) ?: ""
val sonatypePassword = (project.findProperty("sonatypePassword") as String?) ?: ""

apply(plugin = "maven-publish")
apply(plugin = "signing")

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
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
                    username = sonatypeUsername
                    password = sonatypePassword
                }
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
        }
        configure<SigningExtension> {
            sign((extensions["publishing"] as PublishingExtension).publications["maven"])
        }
    }
}
