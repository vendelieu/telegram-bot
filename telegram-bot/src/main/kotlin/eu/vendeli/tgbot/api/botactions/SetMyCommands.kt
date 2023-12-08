@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.builders.BotCommandsBuilder
import eu.vendeli.tgbot.utils.getReturnType

class SetMyCommandsAction(
    languageCode: String? = null,
    scope: BotCommandScope? = null,
    commands: List<BotCommand>,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setMyCommands")
    override val returnType = getReturnType()

    init {
        parameters["commands"] = commands
        if (scope != null) parameters["scope"] = scope
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setMyCommands(languageCode: String? = null, scope: BotCommandScope? = null, command: List<BotCommand>) =
    SetMyCommandsAction(languageCode, scope, command)

@Suppress("NOTHING_TO_INLINE")
inline fun setMyCommands(languageCode: String? = null, scope: BotCommandScope? = null, vararg command: BotCommand) =
    setMyCommands(languageCode, scope, command.asList())
fun setMyCommands(languageCode: String? = null, scope: BotCommandScope? = null, block: BotCommandsBuilder.() -> Unit) =
    setMyCommands(languageCode, scope, BotCommandsBuilder.build(block))
