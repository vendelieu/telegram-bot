package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.types.internal.ParsedText

@Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
internal fun TgUpdateHandler.parseCommand(
    text: String,
): ParsedText = with(bot.config.commandParsing) {
    var state = ParserState.READING_COMMAND
    var command = ""
    val params = mutableMapOf<String, String>()

    var paramNameBuffer = ""
    var paramValBuffer = ""
    var commandAt = ""

    text.forEach { i ->
        when (state) {
            ParserState.READING_COMMAND -> {
                when {
                    i == commandDelimiter || restrictSpacesInCommands && i == ' ' -> {
                        state = ParserState.READING_PARAM_NAME
                    }

                    i == '@' -> {
                        state = ParserState.MATCHING_IDENTIFIER
                    }

                    else -> {
                        command += i
                    }
                }
            }

            ParserState.MATCHING_IDENTIFIER -> {
                if (i == commandDelimiter || (restrictSpacesInCommands && i == ' ')) {
                    if (useIdentifierInGroupCommands && bot.identifier != commandAt)
                        return@with ParsedText(text, emptyMap())
                    state = ParserState.READING_PARAM_NAME
                } else {
                    commandAt += i
                }
            }

            ParserState.READING_PARAM_NAME -> {
                when (i) {
                    parameterValueDelimiter -> {
                        state = ParserState.READING_PARAM_VALUE
                    }

                    parametersDelimiter -> {
                        params["param_${params.size + 1}"] = paramNameBuffer
                        paramNameBuffer = ""
                    }

                    else -> paramNameBuffer += i
                }
            }

            ParserState.READING_PARAM_VALUE -> {
                if (i == parametersDelimiter) {
                    params[paramNameBuffer] = paramValBuffer
                    paramNameBuffer = ""
                    paramValBuffer = ""
                    state = ParserState.READING_PARAM_NAME
                } else {
                    paramValBuffer += i
                }
            }
        }
    }
    if (state == ParserState.READING_PARAM_VALUE) {
        params[paramNameBuffer] = paramValBuffer
    } else if (state == ParserState.READING_PARAM_NAME) {
        params["param_${params.size + 1}"] = paramNameBuffer
    }

    if (params.isEmpty() && command.startsWith("/start ")) {
        params += "deepLink" to command.substringAfter("/start ")
        command = "/start"
    }

    return ParsedText(command = command, params = params)
}

private enum class ParserState {
    READING_COMMAND,
    MATCHING_IDENTIFIER,
    READING_PARAM_NAME,
    READING_PARAM_VALUE,
}
