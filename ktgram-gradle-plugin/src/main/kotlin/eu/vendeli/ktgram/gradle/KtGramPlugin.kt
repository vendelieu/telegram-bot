package eu.vendeli.ktgram.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.targets
import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

abstract class KtGramPlugin : Plugin<Project> {
    private val log = Logging.getLogger(KtGramPlugin::class.java)
    private val libVer = loadPropertyFromResources("ktgram.properties", "ktgram.version")
    private val ktorVer = loadPropertyFromResources("ktgram.properties", "ktgram.ktor")
    private val logbackVer = loadPropertyFromResources("ktgram.properties", "ktgram.logback")

    final override fun apply(project: Project) {
        val pluginExtension = project.extensions.create("ktGram", KtGramExt::class.java)
        val kspPluginPresent = project.plugins.hasPlugin("com.google.devtools.ksp")
        var kspProcessorApplied = false
        val isMultiplatform = project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")

        @Suppress("DEPRECATION")
        val isJvm = project.kotlinExtension.targets.any {
            it.platformType == KotlinPlatformType.jvm || it.platformType == KotlinPlatformType.androidJvm
        }

        project.configurations.configureEach {
            if (name.startsWith("ksp")) dependencies.whenObjectAdded {
                if (group == "eu.vendeli" && name == "ksp") kspProcessorApplied = true
            }
        }
        project.applyDependencies(libVer, isMultiplatform, kspProcessorApplied)

        project.afterEvaluate {
            val targetVer = pluginExtension.forceVersion.getOrElse(libVer)
            if (pluginExtension.addSnapshotRepo.getOrElse(false)) {
                project.repositories.maven {
                    name = "KtGramSnapRepo"
                    setUrl("https://mvn.vendeli.eu/telegram-bot")
                }
            }

            // correct version by forced one
            if (pluginExtension.forceVersion.isPresent) project.configurations.configureEach cfg@{
                dependencies
                    .removeIf {
                        it.group == "eu.vendeli" && (it.name == "telegram-bot" || it.name == "ksp")
                    }.takeIf { it }
                    ?.let {
                        dependencies {
                            add(this@cfg.name, "eu.vendeli:telegram-bot:$targetVer")
                            add(this@cfg.name, "eu.vendeli:ksp:$targetVer")
                        }
                    }
            }

            if (isJvm) {
                val ktorEngine = pluginExtension.ktorJvmEngine.getOrElse(KtorJvmEngine.JAVA)
                handleKtorEngine(ktorEngine)

                handleLoggingProvider()
            }

            project.extensions.configure<KspExtension> {
                pluginExtension.packages.orNull?.takeIf { it.isNotEmpty() }?.joinToString(";")?.let {
                    arg("package", it)
                }
                pluginExtension.autoCleanClassData.getOrElse(true).takeIf { !it }?.let {
                    arg("autoCleanClassData", "false")
                }
            }

            if (!kspPluginPresent) log.error(
                "For full use of `telegram-bot` KSP Gradle Plugin is needed. " +
                    "Add 'com.google.devtools.ksp' plugin to your build, " +
                    "f.e by adding 'id(\"com.google.devtools.ksp\")' to 'plugins' section of the build.gradle[.kts] file",
            )
        }
    }

    private fun Project.handleKtorEngine(engine: KtorJvmEngine) {
        val isNone = engine == KtorJvmEngine.NONE
        val isNotJava = engine != KtorJvmEngine.JAVA

        if (isNone || isNotJava) configurations.configureEach {
            log.debug("Removing ktor-client-java-jvm from $name configuration")
            dependencies.removeIf { it.group == "io.ktor" && it.name == "ktor-client-java-jvm" }
        }
        if (!isNone && isNotJava) {
            log.debug("Adding ktor-client-${engine.artifact}-jvm to $name configuration")
            dependencies.add(
                "implementation",
                "io.ktor:ktor-client-${engine.artifact}-jvm:$ktorVer",
            )
        }
    }

    private fun Project.handleLoggingProvider() {
        log.debug("Checking for logging providers")
        var isProviderPresent = false
        val knownProviders = setOf(
            "ch.qos.logback" to "logback-classic",
            "org.slf4j" to "slf4j-simple",
            "org.slf4j" to "log4j-over-slf4j",
            "org.slf4j" to "slf4j-log4j12",
            "org.slf4j" to "slf4j-jdk14",
            "org.slf4j" to "jul-to-slf4j",
            "org.slf4j" to "slf4j-reload4j",
            "org.slf4j" to "jcl-over-slf4j",
            "org.slf4j" to "slf4j-jcl",
            "org.slf4j" to "slf4j-jdk-platform-logging",
            "org.apache.logging.log4j" to "log4j-slf4j-impl",
            "org.apache.logging.log4j" to "log4j-slf4j2-impl",
            "org.apache.logging.log4j" to "log4j-slf4j18-impl",
            "org.tinylog" to "slf4j-tinylog",
            "org.logevents" to "logevents",
            "com.hkupty.penna" to "penna-core",
            "io.jstach.rainbowgum" to "rainbowgum-core",
        )

        configurations.configureEach {
            dependencies
                .any {
                    it.group to it.name in knownProviders
                }.takeIf { it }
                ?.let {
                    log.debug("Found logging provider in $name configuration")
                    isProviderPresent = true
                }
        }

        if (!isProviderPresent) {
            log.debug("Adding logback-classic to $name configuration")
            dependencies.add(
                "implementation",
                "ch.qos.logback:logback-classic:$logbackVer",
            )
        }
    }

    private fun Project.applyDependencies(depVersion: String, isMultiplatform: Boolean, kspProcessorApplied: Boolean) {
        if (isMultiplatform) extensions.configure<KotlinMultiplatformExtension> {
            targets.forEach { target ->
                val tName = if (target.targetName == "metadata") "CommonMainMetadata"
                else target.targetName.replaceFirstChar { it.uppercaseChar() }

                dependencies.add(
                    "ksp$tName",
                    "eu.vendeli:ksp:$depVersion",
                )
            }
        }

        when {
            kspProcessorApplied -> {}
            isMultiplatform -> {
                extensions.configure(KotlinProjectExtension::class.java) {
                    sourceSets["commonMain"].apply {
                        dependencies {
                            implementation("eu.vendeli:telegram-bot:$depVersion")
                        }
                    }
                }

                dependencies {
                    add("ksp", "eu.vendeli:ksp:$depVersion")
                }
            }

            else -> {
                dependencies.add("implementation", "eu.vendeli:telegram-bot:$depVersion")
                dependencies {
                    add("ksp", "eu.vendeli:ksp:$depVersion")
                }
            }
        }
    }
}
