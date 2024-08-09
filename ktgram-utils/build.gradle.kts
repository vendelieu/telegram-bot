
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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

publishing { addGhRepository() }
mavenPublishing {
    coordinates("eu.vendeli", project.name, project.version.toString())

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
    signAllPublications()

    pom {
        configurePom("KtGram utils", "KtGram library utilities.")
    }

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = true,
        ),
    )
}
