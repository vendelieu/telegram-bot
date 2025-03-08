package eu.vendeli.aide.utils

import org.jetbrains.kotlin.compiler.plugin.CliOption

@Suppress("ktlint:standard:function-naming", "FunctionName")
internal fun Option(name: String, description: String): CliOption = CliOption(
    optionName = name,
    valueDescription = description,
    description = description,
)
