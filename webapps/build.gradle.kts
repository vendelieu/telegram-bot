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


publishing { addGhRepository() }
mavenPublishing {
    coordinates("eu.vendeli", project.name, project.version.toString())

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
    signAllPublications()

    pom {
        configurePom("WebApps", "Implementation of WebApp applications Telegram Bot Api.")
    }

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = true,
        ),
    )
}
