import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private val jvmTargetVer = JvmTarget.JVM_17

private fun KotlinCommonCompilerOptions.configureCompilerOptions() {
    freeCompilerArgs.addAll(
        "-opt-in=eu.vendeli.tgbot.annotations.internal.KtGramInternal",
        "-opt-in=eu.vendeli.tgbot.annotations.internal.ExperimentalFeature",
        "-opt-in=kotlin.time.ExperimentalTime",
        "-Xwarning-level=NOTHING_TO_INLINE:disabled",
        "-Xcontext-parameters"
    )
}

private fun KotlinMultiplatformExtension.configureJvm() {
    jvm {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(jvmTargetVer)
                    freeCompilerArgs.addAll(
                        "-Xjsr305=strict",
                        "-Xjvm-default=all-compatibility",
                    )
                }
            }
        }
    }
    jvmToolchain(jvmTargetVer.target.toInt())
}

fun Project.onlyJvmConfiguredKotlin(block: KotlinMultiplatformExtension.() -> Unit) {
    plugins.apply("kotlin-multiplatform")

    configure<KotlinMultiplatformExtension> {
        compilerOptions { configureCompilerOptions() }
        configureJvm()
        block()
    }
}

fun Project.configuredKotlin(block: KotlinMultiplatformExtension.() -> Unit) {
    plugins.apply("kotlin-multiplatform")

    configure<KotlinMultiplatformExtension> {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions { configureCompilerOptions() }

        configureJvm()
        js { nodejs() }
        mingwX64()
        linuxX64()
        block()
    }
}
