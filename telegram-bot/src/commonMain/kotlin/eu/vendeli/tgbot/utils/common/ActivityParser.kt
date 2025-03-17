package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.types.component.ParsedText

internal fun TgUpdateHandler.parseCommand(
    text: String,
): ParsedText = with(bot.config.commandParsing) {
    var state = CommandParserState.READING_COMMAND
    val commandBuilder = StringBuilder()
    val atTailBuilder = StringBuilder()

    var parsedIndex = 0
    for ((idx, i) in text.withIndex()) {
        parsedIndex = idx
        when (state) {
            CommandParserState.READING_COMMAND -> {
                when {
                    commandDelimiter != ' ' &&
                        !restrictSpacesInCommands &&
                        commandBuilder.length == 6 &&
                        commandBuilder.toString() == "/start" &&
                        i == ' ' -> {
                        // deeplink case
                        break
                    }

                    i == commandDelimiter || (restrictSpacesInCommands && i == ' ') -> {
                        break
                    }

                    i == '@' -> {
                        state = CommandParserState.MATCHING_IDENTIFIER
                    }

                    else -> commandBuilder.append(i)
                }
            }

            CommandParserState.MATCHING_IDENTIFIER -> {
                if (i == commandDelimiter || (restrictSpacesInCommands && i == ' ')) {
                    if (useIdentifierInGroupCommands && bot.config.identifier != atTailBuilder.toString())
                        return@with ParsedText(text, "")
                    break
                } else {
                    atTailBuilder.append(i)
                }
            }
        }
    }

    return ParsedText(commandBuilder.toString(), text.drop(parsedIndex + 1))
}

@KtGramInternal
@Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
fun defaultArgParser(
    text: String,
    parameterDelimiter: Char,
    valueDelimiter: Char,
): Map<String, String> {
    if (text.isEmpty()) return emptyMap()
    var state = ParameterParserState.READING_PARAM_NAME
    val params = mutableMapOf<String, String>()

    val paramNameBuilder = StringBuilder()
    val paramValBuilder = StringBuilder()

    text.forEach { i ->
        when (state) {
            ParameterParserState.READING_PARAM_NAME -> {
                when (i) {
                    valueDelimiter -> {
                        state = ParameterParserState.READING_PARAM_VALUE
                    }

                    parameterDelimiter -> {
                        params["param_${params.size + 1}"] = paramNameBuilder.toString()
                        paramNameBuilder.clear()
                    }

                    else -> paramNameBuilder.append(i)
                }
            }

            ParameterParserState.READING_PARAM_VALUE -> {
                if (i == parameterDelimiter) {
                    params[paramNameBuilder.toString()] = paramValBuilder.toString()
                    paramNameBuilder.clear()
                    paramValBuilder.clear()
                    state = ParameterParserState.READING_PARAM_NAME
                } else {
                    paramValBuilder.append(i)
                }
            }
        }
    }

    // Handle remaining buffers
    if (state == ParameterParserState.READING_PARAM_VALUE) {
        params[paramNameBuilder.toString()] = paramValBuilder.toString()
    } else if (state == ParameterParserState.READING_PARAM_NAME && paramNameBuilder.isNotEmpty()) {
        params["param_${params.size + 1}"] = paramNameBuilder.toString()
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
