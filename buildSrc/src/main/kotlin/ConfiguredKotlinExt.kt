import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private val jvmTargetVer = JvmTarget.JVM_17

private fun KotlinMultiplatformExtension.configureJvm() {
    jvm {
        withJava()
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(jvmTargetVer)
                    freeCompilerArgs = listOf("-Xjsr305=strict")
                }
            }
        }
    }
    jvmToolchain(jvmTargetVer.target.toInt())
}

fun Project.onlyJvmConfiguredKotlin(block: KotlinMultiplatformExtension.() -> Unit) {
    plugins.apply("kotlin-multiplatform")

    configure<KotlinMultiplatformExtension> {
        configureJvm()
        block()
    }
}

fun Project.configuredKotlin(block: KotlinMultiplatformExtension.() -> Unit) {
    plugins.apply("kotlin-multiplatform")

    configure<KotlinMultiplatformExtension> {
        configureJvm()
        js { nodejs() }
        mingwX64()
        linuxX64()
        block()
    }
}
