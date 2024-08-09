import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.credentials
import org.gradle.kotlin.dsl.repositories

fun PublishingExtension.addGhRepository() {
    repositories {
        maven {
            name = "GHPackages"
            setUrl("https://maven.pkg.github.com/vendelieu/telegram-bot")
            credentials(PasswordCredentials::class)
        }
    }
}


fun MavenPom.configurePom(libName: String, libDescription: String) {
    name.set(libName)
    description.set(libDescription)
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

