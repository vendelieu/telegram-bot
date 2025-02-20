package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.types.component.ParsedText
import io.ktor.utils.io.readText
import kotlinx.io.Buffer
import kotlinx.io.bytestring.encodeToByteString
import kotlinx.io.snapshot

private val startCommandBA = "/start".encodeToByteString()

@Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
internal fun TgUpdateHandler.parseCommand(
    text: String,
): ParsedText = with(bot.config.commandParsing) {
    var state = CommandParserState.READING_COMMAND
    val commandBuffer = Buffer()
    val atTailBuffer = Buffer()

    var parsedIndex = 0
    for ((idx, i) in text.withIndex()) {
        parsedIndex = idx
        when (state) {
            CommandParserState.READING_COMMAND -> {
                when {
                    commandDelimiter != ' ' &&
                        !restrictSpacesInCommands &&
                        commandBuffer.size == 6L &&
                        commandBuffer.snapshot() == startCommandBA &&
                        i == ' ' -> {
                        // deeplink case
                        break
                    }

                    i == commandDelimiter || restrictSpacesInCommands && i == ' ' -> {
                        break
                    }

                    i == '@' -> {
                        state = CommandParserState.MATCHING_IDENTIFIER
                    }

                    else -> commandBuffer.writeByte(i.code.toByte())
                }
            }

            CommandParserState.MATCHING_IDENTIFIER -> {
                if (i == commandDelimiter || (restrictSpacesInCommands && i == ' ')) {
                    if (useIdentifierInGroupCommands && bot.config.identifier != atTailBuffer.readText())
                        return@with ParsedText(text, "")
                    break
                } else {
                    atTailBuffer.writeByte(i.code.toByte())
                }
            }
        }
    }

    return ParsedText(commandBuffer.readText(), text.drop(parsedIndex + 1))
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

    val paramNameBuffer = Buffer()
    val paramValBuffer = Buffer()

    text.forEach { i ->
        when (state) {
            ParameterParserState.READING_PARAM_NAME -> {
                when (i) {
                    valueDelimiter -> {
                        state = ParameterParserState.READING_PARAM_VALUE
                    }

                    parameterDelimiter -> {
                        params["param_${params.size + 1}"] = paramNameBuffer.readText()
                    }

                    else -> paramNameBuffer.writeByte(i.code.toByte())
                }
            }

            ParameterParserState.READING_PARAM_VALUE -> {
                if (i == parameterDelimiter) {
                    params[paramNameBuffer.readText()] = paramValBuffer.readText()
                    state = ParameterParserState.READING_PARAM_NAME
                } else {
                    paramValBuffer.writeByte(i.code.toByte())
                }
            }
        }
    }

    if (state == ParameterParserState.READING_PARAM_VALUE) {
        params[paramNameBuffer.readText()] = paramValBuffer.readText()
    } else if (state == ParameterParserState.READING_PARAM_NAME) {
        params["param_${params.size + 1}"] = paramNameBuffer.readText()
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
