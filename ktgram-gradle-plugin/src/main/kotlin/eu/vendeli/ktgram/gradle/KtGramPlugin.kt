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
import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

abstract class KtGramPlugin : Plugin<Project> {
    private val log = Logging.getLogger(KtGramPlugin::class.java)
    private val libVer = loadPropertyFromResources("ktgram.properties", "ktgram.version")
    private val ktorVer = loadPropertyFromResources("ktgram.properties", "ktgram.ktor")

    final override fun apply(project: Project) {
        val pluginExtension = project.extensions.create("ktGram", KtGramExt::class.java)
        val kspPluginPresent = project.plugins.hasPlugin("com.google.devtools.ksp")
        var kspProcessorApplied = false
        val isMultiplatform = project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")

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

            val ktorEngine = pluginExtension.ktorJvmEngine.getOrElse(KtorJvmEngine.JAVA)
            if (ktorEngine != KtorJvmEngine.NONE && ktorEngine != KtorJvmEngine.JAVA) {
                configurations.configureEach {
                    dependencies.removeIf { it.group == "io.ktor" && it.name == "ktor-client-java-jvm" }
                }
                dependencies.add("implementation", "io.ktor:ktor-client-${ktorEngine.artifact}-jvm:$ktorVer")
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
