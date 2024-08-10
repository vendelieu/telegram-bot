val sonatypeUsername = (project.findProperty("sonatypeUsername") as String?) ?: ""
val sonatypePassword = (project.findProperty("sonatypePassword") as String?) ?: ""

plugins {
    `maven-publish`
    signing
    id("tech.yanand.maven-central-publish")
}


val libraryData = extensions.create("libraryData", PublishingExtension::class)

val javadoc by tasks.creating(Jar::class) {
    project.layout.buildDirectory.dir("dokka").orNull?.takeIf { it.asFile.exists() }?.also {
        from(it)
    }
    archiveClassifier.set("javadoc")
}

mavenCentral {
    repoDir = layout.buildDirectory.dir("repos/bundles")
    authToken = System.getenv("SONATYPE_TOKEN")
    publishingType = "AUTOMATIC"
}

publishing {
    publications.configureEach {
        if (this is MavenPublication) {
            if (name != "kotlinMultiplatform") artifact(javadoc)
            pom {
                name = libraryData.name
                description = libraryData.description
                inceptionYear = "2022"
                url = "https://github.com/vendelieu/telegram-bot"

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
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/vendelieu/telegram-bot/issues")
                }
            }
        }
    }
    repositories {
        maven {
            name = "Local"
            setUrl(layout.buildDirectory.dir("repos/bundles"))
        }
        maven {
            name = "GHPackages"
            url = uri("https://maven.pkg.github.com/vendelieu/telegram-bot")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

signing {
    if (sonatypeUsername.isNotEmpty() && sonatypePassword.isNotEmpty()) {
        sign(publishing.publications)
    }
}

tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)
}
