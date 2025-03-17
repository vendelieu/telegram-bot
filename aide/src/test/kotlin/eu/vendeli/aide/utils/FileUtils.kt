package eu.vendeli.aide.utils

import com.tschuchort.compiletesting.SourceFile
import eu.vendeli.aide.processor.AbstractTestProcessor

private const val TEST_PROCESSOR = "// PROCESSOR:"
private const val EXPECTED_RESULT = "// EXPECTED:"
private const val END = "// END"
private const val FILE_NAME = "// FILE:"

fun testData(path: String): TestData = checkNotNull(TestData::class.java.getResourceAsStream("/$path"))
    .bufferedReader()
    .useLines(::processFile)

private fun processFile(lines: Sequence<String>): TestData {
    var processorName = ""
    var fileName = ""
    val processorOutput = mutableListOf<String>()
    val compilerOutput = mutableListOf<String>()
    val sourceFiles = mutableListOf<SourceFile>()
    val source = mutableListOf<String>()
    var current: MutableList<String>? = null
    var trimComment = false
    lines.forEach { line ->
        when {
            line.startsWith(TEST_PROCESSOR) -> {
                check(processorName.isEmpty())
                processorName = line.substringAfter(TEST_PROCESSOR).trim()
                current = processorOutput
                trimComment = true
            }

            line.startsWith(EXPECTED_RESULT) -> {
                current = compilerOutput
                trimComment = true
            }

            line.startsWith(END) -> current = null
            line.startsWith(FILE_NAME) -> {
                if (source.isNotEmpty()) {
                    sourceFiles.add(source.toSourceFile(fileName))
                    source.clear()
                }
                fileName = line.substringAfter(FILE_NAME).trim()
                current = source
                trimComment = false
            }

            else -> current?.add(if (trimComment) line.substring(3) else line)
        }
    }
    if (processorOutput.isNotEmpty()) processorOutput.add("")
    if (compilerOutput.isNotEmpty()) compilerOutput.add("")
    if (source.isNotEmpty()) sourceFiles.add(source.toSourceFile(fileName))
    check(sourceFiles.isNotEmpty())
    val processor =
        Class
            .forName("eu.vendeli.aide.processor.$processorName")
            .getDeclaredConstructor()
            .newInstance() as AbstractTestProcessor
    return TestData(
        processor = processor,
        processorExpectedOutput = processorOutput.joinToString("\n"),
        compilerExpectedOutput = compilerOutput.joinToString("\n"),
        sourceFiles = sourceFiles,
    )
}

private fun MutableList<String>.toSourceFile(fileName: String): SourceFile = when (fileName.substringAfterLast(".")) {
    "kt" -> SourceFile.kotlin(fileName, joinToString("\n"))
    "java" -> SourceFile.java(fileName, joinToString("\n"))
    else -> error("Invalid filename")
}
