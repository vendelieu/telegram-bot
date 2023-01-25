@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.WrappedTypeOf
import eu.vendeli.tgbot.interfaces.getInnerType
import eu.vendeli.tgbot.types.BotCommand
import eu.vendeli.tgbot.types.BotCommandScope
import eu.vendeli.tgbot.types.internal.TgMethod

class GetMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<List<BotCommand>>, WrappedTypeOf<BotCommand> {
    override val method: TgMethod = TgMethod("getMyCommands")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val wrappedDataType = getInnerType()

    init {
        if (scope != null) parameters["scope"] = scope
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

fun getMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    GetMyCommandsAction(scope, languageCode)
