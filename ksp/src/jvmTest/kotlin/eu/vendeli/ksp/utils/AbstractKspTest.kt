package eu.vendeli.ksp.utils

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.PluginOption
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import eu.vendeli.ksp.ActivityProcessorProvider
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

abstract class AbstractKspTest : AnnotationSpec() {
    @OptIn(ExperimentalCompilerApi::class)
    fun compile(
        vararg code: SourceFile,
        options: List<PluginOption> = emptyList(),
        useK2: Boolean = false,
        symbolProcessors: List<SymbolProcessorProvider> = emptyList(),
    ): JvmCompilationResult {
        val compilation = newCompilation(useK2) {
            sources = code.asList()
            symbolProcessorProviders = symbolProcessors.toMutableList()
            pluginOptions = options
            inheritClassPath = true
        }
        return compilation.compile()
    }

    @OptIn(ExperimentalCompilerApi::class)
    fun runTest(
        path: String,
        options: List<PluginOption> = emptyList(),
        useK2: Boolean = false,
        processorProviders: List<SymbolProcessorProvider> = listOf(ActivityProcessorProvider()),
    ) {
        val testData = checkNotNull(javaClass.getResourceAsStream("/$path"))
            .bufferedReader()

        val compilation = newCompilation(useK2) {
            jvmTarget = "17"
            sources = testData
                .lineSequence()
                .toList()
                .toSourceFile(path.substringAfterLast("/"))
                .let { listOf(it) }
            symbolProcessorProviders = processorProviders.toMutableList()
            pluginOptions = options
        }

        val compilationResult = compilation.compile()

        compilationResult.exitCode shouldBe KotlinCompilation.ExitCode.OK

        // Verify generated sources if needed
        val generatedFiles = compilation.kspSourcesDir.walkTopDown().toList()
        generatedFiles.shouldNotBeEmpty()

        generatedFiles.find { it.name.endsWith("ActivitiesData.kt") }.shouldNotBeNull()
        generatedFiles.find { it.name.endsWith("BotCtx.kt") }.shouldNotBeNull()
    }

    @OptIn(ExperimentalCompilerApi::class)
    private inline fun newCompilation(
        useK2: Boolean,
        configure: KotlinCompilation.() -> Unit,
    ) = KotlinCompilation().apply {
        configure()
        verbose = false
        inheritClassPath = true
        languageVersion = if (useK2) "2.0" else "1.9"
        supportsK2 = useK2
    }

    private fun List<String>.toSourceFile(fileName: String): SourceFile =
        when (fileName.substringAfterLast(".")) {
            "kt" -> SourceFile.Companion.kotlin(fileName, joinToString("\n"))
            "java" -> SourceFile.Companion.java(fileName, joinToString("\n"))
            else -> error("Invalid filename")
        }
}
