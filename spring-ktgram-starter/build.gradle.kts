import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.publisher)
}

kotlin {
    jvmToolchain(JAVA_TARGET_V_int)
    jvm()
    sourceSets {
        jvmMain {
            dependencies {
                compileOnly(project(":telegram-bot"))
                compileOnly(libs.ktor.client.core)
                compileOnly(libs.spring.starter)
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
        configurePom("Spring boot starter", "Spring boot starter for KtGram.")
    }

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = true,
        ),
    )
}
