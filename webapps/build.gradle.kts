import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.js.plain.objects)
    alias(libs.plugins.publisher)
}

kotlin {
    js { browser() }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialization)
                implementation(libs.kotlin.datetime)
                implementation(project(":telegram-bot"))
            }
        }
    }
}


publishing {
    repositories {
        maven {
            name = "GHPackages"
            url = uri("https://maven.pkg.github.com/vendelieu/telegram-bot")
            credentials(PasswordCredentials::class)
        }
    }
}
mavenPublishing {
    coordinates("eu.vendeli", project.name, project.version.toString())

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
    signAllPublications()

    pom {
        name.set("WebApps")
        description.set("Implementation of WebApp applications Telegram Bot Api.")
        //
        url.set("https:/github.com/vendelieu/telegram-bot")
        inceptionYear.set("2022")

        licenses {
            license {
                name.set("Apache-2.0")
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
            connection.set("https://github.com/vendelieu/telegram-bot.git")
            url.set("https://github.com/vendelieu/telegram-bot")
        }
        issueManagement {
            system.set("Github")
            url.set("https://github.com/vendelieu/telegram-bot/issues")
        }
    }

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = true,
        ),
    )
}
