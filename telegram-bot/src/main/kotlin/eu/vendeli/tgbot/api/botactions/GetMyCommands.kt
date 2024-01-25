@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<List<BotCommand>>() {
    override val method = TgMethod("getMyCommands")
    override val returnType = getReturnType()

    init {
        if (scope != null) parameters["scope"] = scope.toJsonElement()
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    GetMyCommandsAction(scope, languageCode)
