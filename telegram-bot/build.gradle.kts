import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.time.LocalDate

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compatability.validator)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
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
        commonMain {
            dependencies {
                implementation(libs.kotlin.serialization)
                implementation(libs.kotlin.reflect)

                implementation(libs.krypto)
                implementation(libs.stately)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)

                api(libs.coroutines.core)
                api(libs.kotlin.datetime)
            }
        }
        jvmTest {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.8.1")
                implementation(libs.logback)

                implementation(libs.test.kotest.junit5)
                implementation(libs.test.kotest.assertions)
                implementation(libs.mockk)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.logback)
                implementation(libs.ktor.client.java)
            }
        }
        jsMain {
            dependencies {
                implementation(libs.logging)
                implementation(libs.ktor.client.js)
            }
        }
        named("linuxX64Main") {
            dependencies {
                implementation(libs.logging)
                implementation(libs.ktor.client.curl)
            }
        }
        named("mingwX64Main") {
            dependencies {
                implementation(libs.logging)
                implementation(libs.ktor.client.winhttp)
            }
        }
    }

    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    allWarningsAsErrors.set(true)
                }
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
        name.set("Telegram Bot")
        description.set("Telegram Bot API wrapper, with handy Kotlin DSL.")
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
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true,
        ),
    )
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.from(files("$rootDir/detekt.yml"))
}

buildscript {
    dependencies {
        classpath(libs.dokka.base)
    }
}

tasks {
    register<Kdokker>("kdocUpdate")
    withType<Test> { useJUnitPlatform() }
    dokkaHtml.configure {
        outputDirectory.set(layout.buildDirectory.asFile.orNull?.resolve("dokka"))
        dokkaSourceSets {
            named("commonMain") { sourceRoots.setFrom(project.projectDir.resolve("src/commonMain/kotlin")) }
            named("jvmMain") { sourceRoots.setFrom(project.projectDir.resolve("src/jvmMain/kotlin")) }
            named("jsMain") { sourceRoots.setFrom(project.projectDir.resolve("src/jsMain/kotlin")) }
            if ("nativeMain" in names) named("nativeMain") {
                sourceRoots.setFrom(project.projectDir.resolve("src/nativeMain/kotlin"))
            }
            collectionSchema.elements.forEach { _ -> moduleName.set("Telegram Bot") }
        }
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            customStyleSheets = listOf(rootDir.resolve("assets/logo-styles.css"))
            customAssets = listOf(rootDir.resolve("assets/tgbotkt-logo.png"))
            footerMessage = "© ${LocalDate.now().year} Vendelieu"
        }
    }
}

apiValidation {
    ignoredPackages.add("utils")
    nonPublicMarkers.apply {
        add("eu.vendeli.tgbot.annotations.internal.ExperimentalFeature")
        add("eu.vendeli.tgbot.annotations.internal.InternalApi")
    }
}

kover {
    reports {
        filters {
            excludes {
                packages(
                    "eu.vendeli.tgbot.interfaces",
                    "eu.vendeli.tgbot.types",
                    "eu.vendeli.tgbot.utils",
                )
                classes(
                    "eu.vendeli.tgbot.api.botactions.Close*", // test is ignored
                    "eu.vendeli.tgbot.api.botactions.Logout*",
                    "eu.vendeli.tgbot.api.stickerset.*CustomEmoji*",
                    "eu.vendeli.tgbot.implementations.EnvConfigLoader*",
                )
            }
        }
    }
}

