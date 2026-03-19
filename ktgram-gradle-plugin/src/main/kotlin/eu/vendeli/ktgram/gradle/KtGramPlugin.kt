package eu.vendeli.ktgram.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
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
            ext.findByType(KotlinMultiplatformExtension::class.java)?.targets?.any {
                it.platformType == KotlinPlatformType.jvm
            } ?: ext.findByType(KotlinJvmExtension::class.java)
        } != null

        target.configurations.configureEach { config ->
            if (config.name.startsWith("ktnip")) config.dependencies.whenObjectAdded { dep ->
                if (dep.group == "eu.vendeli" && dep.name == "ktnip") kspProcessorApplied = true
            }
        }
        target.applyDependencies(libVer, isMultiplatform, kspProcessorApplied)

        target.afterEvaluate {
            val targetVer = pluginExtension.forceVersion.getOrElse(libVer)
            if (pluginExtension.addSnapshotRepo.getOrElse(false)) {
                target.repositories.maven { repo ->
                    repo.name = "KtGramSnapRepo"
                    repo.setUrl(java.net.URI.create("https://mvn.vendeli.eu/telegram-bot"))
                }
            }

            // correct version by forced one
            if (pluginExtension.forceVersion.isPresent) target.configurations.configureEach { config ->
                val toRemove = config.dependencies
                    .filter {
                        it.group == "eu.vendeli" && (it.name == "telegram-bot" || it.name == "ktnip")
                    }.toList()
                toRemove.forEach { config.dependencies.remove(it) }
                if (toRemove.isNotEmpty()) {
                    target.dependencies.add(config.name, "eu.vendeli:telegram-bot:$targetVer")
                    target.dependencies.add(config.name, "eu.vendeli:ktnip:$targetVer")
                }
            }

            if (isJvm) {
                val ktorEngine = pluginExtension.ktorJvmEngine.getOrElse(KtorJvmEngine.JAVA)
                target.handleKtorEngine(ktorEngine)

                val handleLoggingProvider = pluginExtension.handleLoggingProvider.getOrElse(true)
                if (handleLoggingProvider) target.handleLoggingProvider()
            }

            target.extensions.configure(KspExtension::class.java) { ksp ->
                pluginExtension.packages.orNull?.takeIf { it.isNotEmpty() }?.joinToString(";")?.let {
                    ksp.arg("package", it)
                }
                pluginExtension.autoAnswerCallback.getOrElse(false).takeIf { it }?.let {
                    ksp.arg("autoAnswerCallback", "true")
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

        if (isNone || isNotJava) configurations.configureEach { config ->
            log.debug("Removing ktor-client-java-jvm from ${config.name} configuration")
            config.dependencies
                .filter { it.group == "io.ktor" && it.name == "ktor-client-java-jvm" }
                .toList()
                .forEach { config.dependencies.remove(it) }
        }
        if (!isNone && isNotJava) {
            log.debug("Adding ktor-client-${engine.artifact}-jvm to configuration")
            dependencies.add(
                "implementation",
                "io.ktor:ktor-client-${engine.artifact}-jvm:$ktorVer",
            )
        }
    }

    private fun Project.handleLoggingProvider() {
        log.debug("Checking for logging providers")
        var isProviderPresent = false

        configurations.configureEach { config ->
            val hasLogger = config.dependencies.any {
                (it.group to it.name) in loggerProviders
            }
            if (hasLogger) {
                log.debug("Found logging provider in ${config.name} configuration")
                isProviderPresent = true
            }
        }

        if (!isProviderPresent) {
            log.debug("Adding logback-classic to configuration")
            dependencies.add(
                "implementation",
                "ch.qos.logback:logback-classic:$logbackVer",
            )
        }
    }

    private fun Project.applyDependencies(depVersion: String, isMultiplatform: Boolean, kspProcessorApplied: Boolean) {
        if (isMultiplatform) extensions.configure(KotlinMultiplatformExtension::class.java) { kmp ->
            kmp.targets.forEach { kotlinTarget ->
                val tName = if (kotlinTarget.targetName == "metadata") "CommonMainMetadata"
                else kotlinTarget.targetName.replaceFirstChar { it.uppercaseChar() }

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
                    this@applyDependencies.dependencies.add(
                        "commonMainImplementation",
                        "eu.vendeli:telegram-bot:$depVersion",
                    )
                }

                dependencies.add("ksp", "eu.vendeli:ktnip:$depVersion")
            }

            else -> {
                dependencies.add("implementation", "eu.vendeli:telegram-bot:$depVersion")
                dependencies.add("ksp", "eu.vendeli:ktnip:$depVersion")
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
