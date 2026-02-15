package eu.vendeli.ktnip.utils

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.PluginOption
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import eu.vendeli.ktnip.ActivityProcessorProvider
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

abstract class AbstractKspTest : AnnotationSpec() {
    @OptIn(ExperimentalCompilerApi::class)
    fun compile(
        vararg code: SourceFile,
        options: List<PluginOption> = emptyList(),
        symbolProcessors: List<SymbolProcessorProvider> = emptyList(),
    ): JvmCompilationResult {
        val compilation = newCompilation {
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
        processorProviders: List<SymbolProcessorProvider> = listOf(ActivityProcessorProvider()),
    ) {
        val sourceContent = checkNotNull(javaClass.getResourceAsStream("/$path"))
            .bufferedReader()
            .use { it.readText() }

        val compilation = newCompilation {
            jvmTarget = "17"
            sources = sourceContent
                .lineSequence()
                .toList()
                .toSourceFile(path.substringAfterLast("/"))
                .let { listOf(it) }
            symbolProcessorProviders = processorProviders.toMutableList()
            pluginOptions = options
        }

        val compilationResult = compilation.compile()

        compilationResult.exitCode shouldBe KotlinCompilation.ExitCode.OK

        // Verify generated sources
        val generatedFiles = compilation.kspSourcesDir.walkTopDown().toList()
        generatedFiles.shouldNotBeEmpty()
        generatedFiles.find { it.name.endsWith("ActivitiesData.kt") }.shouldNotBeNull()
        generatedFiles.find { it.name.endsWith("KtGramCtxLoader.kt") }.shouldNotBeNull()
        generatedFiles.find { it.name.endsWith("BotCtx.kt") }.shouldNotBeNull()

        // Verify golden output expectations from // @generated comments
        val expectations = parseGeneratedExpectations(sourceContent)
        for ((fileName, substrings) in expectations) {
            val candidates = generatedFiles
                .filter { it.isFile }
                .filter { it.name == fileName || it.name.endsWith(fileName) }
            val file = candidates
                .filter { it.absolutePath.contains("generated", ignoreCase = true) }
                .maxByOrNull { it.absolutePath.length }
                ?: candidates.firstOrNull()
                ?: error("Generated file '$fileName' not found for expectations")
            val content = file.readText()
            for (substring in substrings) {
                content.shouldContain(substring)
            }
        }
    }

    /**
     * Parses // @generated <filename>: [substrings] comments from source.
     * Supports single-line (comma-separated) and multi-line (indented with //   or // -) formats.
     */
    private fun parseGeneratedExpectations(source: String): Map<String, List<String>> {
        val result = mutableMapOf<String, MutableList<String>>()
        val headerRegex = Regex("""// @generated (\S+):\s*(.*)""")
        val continuationRegex = Regex("""//[\s-]+(.+)""")
        var inMultiline = false
        var currentFile: String? = null

        for (line in source.lines()) {
            val headerMatch = headerRegex.find(line)
            if (headerMatch != null) {
                inMultiline = false
                val filename = headerMatch.groupValues[1]
                val inlineContent = headerMatch.groupValues[2].trim()
                if (inlineContent.isNotEmpty()) {
                    result.getOrPut(filename) { mutableListOf() }.addAll(
                        inlineContent.split(',').map { it.trim() }.filter { it.isNotEmpty() },
                    )
                } else {
                    inMultiline = true
                    currentFile = filename
                }
                continue
            }
            if (inMultiline && currentFile != null) {
                val contMatch = continuationRegex.find(line)
                if (contMatch != null) {
                    result.getOrPut(currentFile) { mutableListOf() }.add(contMatch.groupValues[1].trim())
                } else if (line.isBlank() || !line.trimStart().startsWith("//")) {
                    inMultiline = false
                    currentFile = null
                }
            }
        }
        return result
    }

    @OptIn(ExperimentalCompilerApi::class)
    private inline fun newCompilation(
        configure: KotlinCompilation.() -> Unit,
    ) = KotlinCompilation().apply {
        useKsp2()
        configure()
        verbose = false
        inheritClassPath = true
        languageVersion = "2.2"
    }

    private fun List<String>.toSourceFile(fileName: String): SourceFile =
        when (fileName.substringAfterLast(".")) {
            "kt" -> SourceFile.kotlin(fileName, joinToString("\n"))
            "java" -> SourceFile.java(fileName, joinToString("\n"))
            else -> error("Invalid filename")
        }
}
