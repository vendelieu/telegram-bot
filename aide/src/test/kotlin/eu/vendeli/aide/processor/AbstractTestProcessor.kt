package eu.vendeli.aide.processor

import com.tschuchort.compiletesting.JvmCompilationResult
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.ir.declarations.IrFile

abstract class AbstractTestProcessor {
    /**
     * This class is used to pass necessary data to the [AbstractTestProcessor] for analysis.
     *
     * It contains the intermediate representation [file] that will be analysed. This file provides
     * necessary information about the structure of the source code. When needed, a [context] object
     * that provides access to useful APIs within the compilation unit is also exposed.
     */
    class AnalysisData(
        val file: IrFile,
        val context: IrPluginContext,
    )

    private val processorOutput = StringBuilder()

    /**
     * Analyses the given [data], extracts necessary information as the case may be, and serializes it
     * into text format which is then written into a buffer.
     */
    fun analyse(data: AnalysisData) {
        processorOutput.analyse(data)
    }

    /**
     * Processes the given [data] and serializes the result into the receiving [StringBuilder].
     *
     * This method is invoked during the compilation phase to analyse the source code IR and extract
     * necessary information for the test. The implementation of this method should be kept as simple
     * as possible, as it is not intended to perform any complex logic.
     */
    abstract fun StringBuilder.analyse(data: AnalysisData)

    /**
     * Returns the processor analysis result.
     *
     * This is the result that will be compared against the expected result.
     */
    fun getProcessorResult(): String = processorOutput.toString()

    /**
     * Extracts the compiler result from the given [JvmCompilationResult].
     *
     * The default implementation simply sanitizes the compiler output, removing unnecessary lines and
     * replacing temporary file names with a generic placeholder to ensure reproducibility.
     */
    @OptIn(ExperimentalCompilerApi::class)
    open fun extractCompilerResult(
        compilationResult: JvmCompilationResult,
    ): String = extractCompilerMessage(compilationResult)

    companion object {
        private val REGEX_FILENAME = "file:///tmp/[-\\w]+/sources/(.+).(kt|java)".toRegex()

        @OptIn(ExperimentalCompilerApi::class)
        fun extractCompilerMessage(result: JvmCompilationResult): String = result.messages
            .splitToSequence("\n")
            .dropWhile { it.startsWith("w: Default scripting plugin is disabled") }
            .joinToString("\n") { it.replace(REGEX_FILENAME, "file:///$1.$2") }
    }
}
