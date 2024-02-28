@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<List<BotCommand>>() {
    override val method = TgMethod("getMyCommands")
    override val returnType = getReturnType()

    init {
        if (scope != null) parameters["scope"] = scope.encodeWith(BotCommandScope.serializer())
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to get the current list of the bot's commands for the given scope and user language. Returns an Array of BotCommand objects. If commands aren't set, an empty list is returned.
 * @param scope A JSON-serialized object, describing scope of users. Defaults to BotCommandScopeDefault.
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 * @returns [Array of BotCommand]
 * Api reference: https://core.telegram.org/bots/api#getmycommands
*/
@Suppress("NOTHING_TO_INLINE")
inline fun getMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    GetMyCommandsAction(scope, languageCode)
