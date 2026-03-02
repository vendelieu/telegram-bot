@file:OptIn(org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi::class)

package eu.vendeli.ktnip.utils.ksptest

import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import eu.vendeli.ktnip.utils.ksptest.models.GExpectBlock
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import java.io.File

/**
 * Applies G-EXPECT file and error expectations against a compilation result.
 *
 * @param requiredFiles When expecting success, these generated files must exist.
 *                      Default: ActivitiesData.kt, KtGramCtxLoader.kt, BotCtx.kt
 */
class ExpectationAssertor(
    private val requiredFiles: List<String> = listOf(
        "ActivitiesData.kt",
        "KtGramCtxLoader.kt",
        "BotCtx.kt",
    ),
    private val fileResolver: GeneratedFileResolver = GeneratedFileResolver,
) {

    fun assert(
        kspSourcesDir: File,
        result: JvmCompilationResult,
        expectations: GExpectBlock,
        testName: String,
        classLoader: ClassLoader,
    ) {
        val expectSuccess = expectations.noError || expectations.errorExpectations.isEmpty()
        val updateMode = GoldenFileHandler.isUpdateMode()
        val goldenOutputDir = GoldenFileHandler.getGoldenOutputDir()

        if (expectSuccess) {
            result.exitCode shouldBe ExitCode.OK
            val generatedFiles = kspSourcesDir.walkTopDown().toList()
            generatedFiles.shouldNotBeEmpty()
            for (required in requiredFiles) {
                generatedFiles.find { it.name.endsWith(required) }.shouldNotBeNull()
            }
        } else {
            result.exitCode shouldBe ExitCode.COMPILATION_ERROR
            assertErrorExpectations(result.messages, expectations.errorExpectations)
        }

        val generatedFiles = kspSourcesDir.walkTopDown().toList()
        for (fe in expectations.fileExpectations) {
            assertOrUpdateFileExpectation(
                kspSourcesDir = kspSourcesDir,
                generatedFiles = generatedFiles,
                fe = fe,
                testName = testName,
                classLoader = classLoader,
                updateMode = updateMode,
                goldenOutputDir = goldenOutputDir,
            )
        }
    }

    private fun assertErrorExpectations(messages: String, errorExpectations: List<eu.vendeli.ktnip.utils.ksptest.models.ErrorExpectation>) {
        for (err in errorExpectations) {
            err.contains?.let { messages.shouldContain(it) }
            err.exact?.let { messages.shouldContain(it) }
            err.count?.let { count ->
                val errorLines = messages.lines().filter { it.trimStart().startsWith("e:") }
                errorLines.size shouldBe count
            }
            when {
                err.file != null && err.line != null && err.messageContains != null -> {
                    val matching = messages.lines()
                        .filter {
                            it.contains(err.file) &&
                                it.contains("(${err.line},") &&
                                it.contains(err.messageContains)
                        }
                    matching.shouldNotBeEmpty()
                }
                else -> Unit
            }
        }
    }

    private fun assertOrUpdateFileExpectation(
        kspSourcesDir: File,
        generatedFiles: List<File>,
        fe: eu.vendeli.ktnip.utils.ksptest.models.FileExpectation,
        testName: String,
        classLoader: ClassLoader,
        updateMode: Boolean,
        goldenOutputDir: File?,
    ) {
        val file = fileResolver.resolve(fe.file, kspSourcesDir)
        val content = file.readText()

        val hasGolden = GoldenFileHandler.hasGoldenFile(classLoader, testName, fe.file)

        when {
            updateMode && goldenOutputDir != null -> {
                GoldenFileHandler.writeGoldenFile(goldenOutputDir, testName, fe.file, content)
            }
            hasGolden -> {
                val goldenContent = GoldenFileHandler.loadGoldenContent(classLoader, testName, fe.file)
                    ?: error("Golden file exists but could not be loaded: ${GoldenFileHandler.goldenResourcePath(testName, fe.file)}")
                content shouldBe goldenContent
            }
            else -> {
                for (s in fe.contains) content.shouldContain(s)
                for (s in fe.notContains) content.shouldNotContain(s)
                for (pattern in fe.matches) {
                    Regex(pattern).containsMatchIn(content) shouldBe true
                }
                for (pattern in fe.notMatches) {
                    Regex(pattern).containsMatchIn(content) shouldBe false
                }
            }
        }
    }
}
