package eu.vendeli.aide.utils

import com.tschuchort.compiletesting.SourceFile
import eu.vendeli.aide.processor.AbstractTestProcessor

class TestData(
    val processor: AbstractTestProcessor,
    val processorExpectedOutput: String,
    val compilerExpectedOutput: String,
    val sourceFiles: List<SourceFile>,
)
