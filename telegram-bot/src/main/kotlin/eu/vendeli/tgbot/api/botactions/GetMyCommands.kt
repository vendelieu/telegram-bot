@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getCollectionReturnType

class GetMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<List<BotCommand>>() {
    override val method = TgMethod("getMyCommands")
    override val collectionReturnType = getCollectionReturnType()

    init {
        if (scope != null) parameters["scope"] = scope
        if (languageCode != null) parameters["language_code"] = languageCode
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    GetMyCommandsAction(scope, languageCode)
