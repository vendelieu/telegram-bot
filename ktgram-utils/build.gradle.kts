import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.publisher)
}

kotlin {
    jvm {
        withJava()
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(JAVA_TARGET_V))
                }
            }
        }
    }
    js { nodejs() }
    mingwX64()
    linuxX64()
    jvmToolchain(JAVA_TARGET_V_int)

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":telegram-bot"))
                implementation(libs.kotlin.datetime)
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
        name.set("KtGram utils")
        description.set("KtGram library utilities.")
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
