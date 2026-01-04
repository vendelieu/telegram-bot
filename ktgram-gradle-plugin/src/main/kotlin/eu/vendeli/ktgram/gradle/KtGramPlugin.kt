package eu.vendeli.ktgram.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

abstract class KtGramPlugin : KotlinCompilerPluginSupportPlugin {
    private val log = Logging.getLogger(KtGramPlugin::class.java)
    private val libVer = loadPropertyFromResources("ktgram.properties", "ktgram.version")
    private val ktorVer = loadPropertyFromResources("ktgram.properties", "ktgram.ktor")
    private val logbackVer = loadPropertyFromResources("ktgram.properties", "ktgram.logback")

    final override fun apply(target: Project) {
        val pluginExtension = target.extensions.create("ktGram", KtGramExt::class.java)
        val kspPluginPresent = target.plugins.hasPlugin("com.google.devtools.ksp")
        var kspProcessorApplied = false
        val isMultiplatform = target.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")

        val isJvm = target.project.extensions.let { ext ->
            ext.findByType<KotlinMultiplatformExtension>()?.targets?.any {
                it.platformType == KotlinPlatformType.jvm
            } ?: ext.findByType<KotlinJvmExtension>()
        } != null

        target.configurations.configureEach {
            if (name.startsWith("ktnip")) dependencies.whenObjectAdded {
                if (group == "eu.vendeli" && name == "ktnip") kspProcessorApplied = true
            }
        }
        target.applyDependencies(libVer, isMultiplatform, kspProcessorApplied)

        target.afterEvaluate {
            val targetVer = pluginExtension.forceVersion.getOrElse(libVer)
            if (pluginExtension.addSnapshotRepo.getOrElse(false)) {
                target.repositories.maven {
                    name = "KtGramSnapRepo"
                    setUrl("https://mvn.vendeli.eu/telegram-bot")
                }
            }

            // correct version by forced one
            if (pluginExtension.forceVersion.isPresent) target.configurations.configureEach cfg@{
                dependencies
                    .removeIf {
                        it.group == "eu.vendeli" && (it.name == "telegram-bot" || it.name == "ktnip")
                    }.takeIf { it }
                    ?.let {
                        dependencies {
                            add(this@cfg.name, "eu.vendeli:telegram-bot:$targetVer")
                            add(this@cfg.name, "eu.vendeli:ktnip:$targetVer")
                        }
                    }
            }

            if (isJvm) {
                val ktorEngine = pluginExtension.ktorJvmEngine.getOrElse(KtorJvmEngine.JAVA)
                handleKtorEngine(ktorEngine)

                val handleLoggingProvider = pluginExtension.handleLoggingProvider.getOrElse(true)
                if (handleLoggingProvider) handleLoggingProvider()
            }

            target.extensions.configure<KspExtension> {
                pluginExtension.packages.orNull?.takeIf { it.isNotEmpty() }?.joinToString(";")?.let {
                    arg("package", it)
                }
                pluginExtension.autoAnswerCallback.getOrElse(false).takeIf { it }?.let {
                    arg("autoAnswerCallback", "true")
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

        configurations.configureEach {
            dependencies
                .any {
                    it.group to it.name in loggerProviders
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
                    "eu.vendeli:ktnip:$depVersion",
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
                    add("ksp", "eu.vendeli:ktnip:$depVersion")
                }
            }

            else -> {
                dependencies.add("implementation", "eu.vendeli:telegram-bot:$depVersion")
                dependencies {
                    add("ksp", "eu.vendeli:ktnip:$depVersion")
                }
            }
        }
    }

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "eu.vendeli",
        artifactId = "ktnip",
        version = libVer,
    )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = false

    override fun getCompilerPluginId(): String = "eu.vendeli.ktnip"

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>,
    ): Provider<List<SubpluginOption>> = kotlinCompilation.target.project.provider {
        emptyList()
    }
}
