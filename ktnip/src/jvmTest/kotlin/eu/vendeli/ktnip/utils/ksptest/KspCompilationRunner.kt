@file:OptIn(org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi::class)

package eu.vendeli.ktnip.utils.ksptest

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.*

/**
 * Builds and runs KSP compilation for tests.
 */
class KspCompilationRunner(
    private val jvmTarget: String = "17",
    private val languageVersion: String = "2.2",
) {

    fun compile(
        sourceContent: String,
        sourceFileName: String,
        options: List<PluginOption> = emptyList(),
        processorProviders: List<SymbolProcessorProvider>,
    ): CompilationRun {
        val sourceFile = toSourceFile(sourceFileName, sourceContent)
        val compilation = KotlinCompilation().apply {
            useKsp2()
            jvmTarget = this@KspCompilationRunner.jvmTarget
            languageVersion = this@KspCompilationRunner.languageVersion
            sources = listOf(sourceFile)
            symbolProcessorProviders = processorProviders.toMutableList()
            pluginOptions = options
            verbose = false
            inheritClassPath = true
        }
        val result = compilation.compile()
        return CompilationRun(
            kspSourcesDir = compilation.kspSourcesDir,
            result = result,
        )
    }

    private fun toSourceFile(fileName: String, content: String): SourceFile =
        when (fileName.substringAfterLast(".")) {
            "kt" -> SourceFile.kotlin(fileName, content)
            "java" -> SourceFile.java(fileName, content)
            else -> error("Invalid filename: $fileName")
        }

    data class CompilationRun(
        val kspSourcesDir: java.io.File,
        val result: JvmCompilationResult,
    )
}
