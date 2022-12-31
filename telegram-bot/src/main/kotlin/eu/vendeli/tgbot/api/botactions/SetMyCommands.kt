@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.BotCommand
import eu.vendeli.tgbot.types.BotCommandScope
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.builders.BotCommandsBuilder

class SetMyCommandsAction(
    commands: List<BotCommand>,
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("setMyCommands")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["commands"] = commands
        if (scope != null) parameters["scope"] = scope
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun setMyCommands(languageCode: String? = null, scope: BotCommandScope? = null, vararg command: BotCommand) =
    SetMyCommandsAction(listOf(*command), scope, languageCode)

fun setMyCommands(languageCode: String? = null, scope: BotCommandScope? = null, block: BotCommandsBuilder.() -> Unit) =
    SetMyCommandsAction(BotCommandsBuilder().apply(block).commandsList, scope, languageCode)
