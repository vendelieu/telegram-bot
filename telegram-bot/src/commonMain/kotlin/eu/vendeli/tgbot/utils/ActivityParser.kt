package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.types.internal.ParsedText

@Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
internal fun TgUpdateHandler.parseCommand(
    text: String,
): ParsedText = with(bot.config.commandParsing) {
    var state = CommandParserState.READING_COMMAND
    var command = ""
    var commandAt = ""

    var parsedIndex = 0
    for ((idx, i) in text.withIndex()) {
        parsedIndex = idx
        when (state) {
            CommandParserState.READING_COMMAND -> {
                when {
                    commandDelimiter != ' ' && !restrictSpacesInCommands && command == "/start" -> {
                        // deeplink case
                        if (i == ' ') break
                    }

                    i == commandDelimiter || restrictSpacesInCommands && i == ' ' -> {
                        break
                    }

                    i == '@' -> {
                        state = CommandParserState.MATCHING_IDENTIFIER
                    }

                    else -> {
                        command += i
                    }
                }
            }

            CommandParserState.MATCHING_IDENTIFIER -> {
                if (i == commandDelimiter || (restrictSpacesInCommands && i == ' ')) {
                    if (useIdentifierInGroupCommands && bot.config.identifier != commandAt)
                        return@with ParsedText(text, "")
                    break
                } else {
                    commandAt += i
                }
            }
        }
    }

    return ParsedText(command, text.drop(parsedIndex + 1))
}

@InternalApi
@Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
fun defaultArgParser(
    text: String,
    parameterDelimiter: Char,
    valueDelimiter: Char,
): Map<String, String> {
    if (text.isEmpty()) return emptyMap()
    var state = ParameterParserState.READING_PARAM_NAME
    val params = mutableMapOf<String, String>()

    var paramNameBuffer = ""
    var paramValBuffer = ""

    text.forEach { i ->
        when (state) {
            ParameterParserState.READING_PARAM_NAME -> {
                when (i) {
                    valueDelimiter -> {
                        state = ParameterParserState.READING_PARAM_VALUE
                    }

                    parameterDelimiter -> {
                        params["param_${params.size + 1}"] = paramNameBuffer
                        paramNameBuffer = ""
                    }

                    else -> paramNameBuffer += i
                }
            }

            ParameterParserState.READING_PARAM_VALUE -> {
                if (i == parameterDelimiter) {
                    params[paramNameBuffer] = paramValBuffer
                    paramNameBuffer = ""
                    paramValBuffer = ""
                    state = ParameterParserState.READING_PARAM_NAME
                } else {
                    paramValBuffer += i
                }
            }
        }
    }

    if (state == ParameterParserState.READING_PARAM_VALUE) {
        params[paramNameBuffer] = paramValBuffer
    } else if (state == ParameterParserState.READING_PARAM_NAME) {
        params["param_${params.size + 1}"] = paramNameBuffer
    }

    return params
}

private enum class CommandParserState {
    READING_COMMAND,
    MATCHING_IDENTIFIER,
}

private enum class ParameterParserState {
    READING_PARAM_NAME,
    READING_PARAM_VALUE,
}
