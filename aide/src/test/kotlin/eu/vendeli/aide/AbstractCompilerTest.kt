package eu.vendeli.aide

import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.PluginOption
import com.tschuchort.compiletesting.SourceFile
import eu.vendeli.aide.utils.testData
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

abstract class AbstractCompilerTest : AnnotationSpec() {
    private val processor = AideCommandLineProcessor()

    @OptIn(ExperimentalCompilerApi::class)
    fun compile(
        vararg code: SourceFile,
        options: List<PluginOption> = listOf(AideCommandLineProcessor.AUTO_SEND.asOption("true")),
        useK2: Boolean = false,
    ): JvmCompilationResult {
        val compilation =
            newCompilation(useK2) {
                sources = code.asList()
                compilerPluginRegistrars = listOf(AideCompilerPluginRegistrar())
                pluginOptions = options
            }
        return compilation.compile()
    }

    @OptIn(ExperimentalCompilerApi::class)
    fun runTest(
        path: String,
        options: List<PluginOption> = listOf(AideCommandLineProcessor.AUTO_SEND.asOption("true")),
        useK2: Boolean = false,
    ) {
        val testData = testData(path)
        val compilation = newCompilation(useK2) {
            jvmTarget = "17"
            sources = testData.sourceFiles
            compilerPluginRegistrars =
                listOf(AideCompilerPluginRegistrar(), TestComponentRegistrar(testData.processor))
            pluginOptions = options
        }

        val compilationResult = compilation.compile()
        val processorActualResult = testData.processor.getProcessorResult()
        val compilerActualResult = testData.processor.extractCompilerResult(compilationResult)

        processorActualResult shouldBe testData.processorExpectedOutput
        compilerActualResult shouldBe testData.compilerExpectedOutput
    }

    fun String.asOption(value: String): PluginOption = PluginOption(
        processor.pluginId,
        this,
        value,
    )

    @OptIn(ExperimentalCompilerApi::class)
    private inline fun newCompilation(useK2: Boolean, configure: KotlinCompilation.() -> Unit) =
        KotlinCompilation().apply {
            configure()
            verbose = false
            commandLineProcessors = listOf(processor)
            inheritClassPath = true
            languageVersion = if (useK2) "2.0" else "1.9"
            supportsK2 = useK2
        }
}
