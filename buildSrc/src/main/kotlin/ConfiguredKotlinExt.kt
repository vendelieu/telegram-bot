
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private val jvmTargetVer = JvmTarget.JVM_17

fun Project.onlyJvmConfiguredKotlin(block: KotlinMultiplatformExtension.() -> Unit) {
    plugins.apply("kotlin-multiplatform")

    configure<KotlinMultiplatformExtension> {
        jvm {
            withJava()
            compilations.all {
                compileTaskProvider.configure {
                    compilerOptions {
                        jvmTarget.set(jvmTargetVer)
                    }
                }
            }
        }
        jvmToolchain(jvmTargetVer.target.toInt())
        block()
    }
}

fun Project.configuredKotlin(block: KotlinMultiplatformExtension.() -> Unit) {
    plugins.apply("kotlin-multiplatform")

    configure<KotlinMultiplatformExtension> {
        jvm {
            withJava()
            compilations.all {
                compileTaskProvider.configure {
                    compilerOptions {
                        jvmTarget.set(jvmTargetVer)
                    }
                }
            }
        }
        js { nodejs() }
        mingwX64()
        linuxX64()
        jvmToolchain(jvmTargetVer.target.toInt())
        block()
    }
}
