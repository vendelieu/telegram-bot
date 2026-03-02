package eu.vendeli.ktnip.utils

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.*
import eu.vendeli.ktnip.ActivityProcessorProvider
import eu.vendeli.ktnip.utils.ksptest.ExpectationAssertor
import eu.vendeli.ktnip.utils.ksptest.GExpectParser
import eu.vendeli.ktnip.utils.ksptest.GoldenFileHandler
import eu.vendeli.ktnip.utils.ksptest.KspCompilationRunner
import eu.vendeli.ktnip.utils.ksptest.models.FileExpectation
import eu.vendeli.ktnip.utils.ksptest.models.GExpectBlock
import io.kotest.core.spec.style.AnnotationSpec
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

abstract class AbstractKspTest : AnnotationSpec() {

    private val compilationRunner = KspCompilationRunner()
    private val assertor = ExpectationAssertor()

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

        val run = compilationRunner.compile(
            sourceContent = sourceContent,
            sourceFileName = path.substringAfterLast("/"),
            options = options,
            processorProviders = processorProviders,
        )

        val testName = path.substringAfterLast("/").substringBeforeLast(".")
        val expectations = GExpectParser.parse(sourceContent)
            ?: buildExpectationsFromGolden(testName)
            ?: error("Test data must contain a /* G-EXPECT ... */ block or golden files. Path: $path")
        assertor.assert(
            kspSourcesDir = run.kspSourcesDir,
            result = run.result,
            expectations = expectations,
            testName = testName,
            classLoader = javaClass.classLoader,
        )
    }

    /**
     * Builds expectations from golden files when G-EXPECT is absent.
     * Returns null if no golden files exist for this test.
     */
    private fun buildExpectationsFromGolden(testName: String): GExpectBlock? {
        val goldenFiles = GoldenFileHandler.discoverGoldenFiles(javaClass.classLoader, testName)
        if (goldenFiles.isEmpty()) return null
        return GExpectBlock(
            fileExpectations = goldenFiles.map { fileName ->
                FileExpectation(
                    file = fileName,
                    contains = emptyList(),
                    notContains = emptyList(),
                    matches = emptyList(),
                    notMatches = emptyList(),
                )
            },
            errorExpectations = emptyList(),
            noError = true,
        )
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
}
