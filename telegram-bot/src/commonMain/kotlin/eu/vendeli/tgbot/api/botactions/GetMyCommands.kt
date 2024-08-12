@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class GetMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<List<BotCommand>>() {
    override val method = "getMyCommands"
    override val returnType = getReturnType()

    init {
        if (scope != null) parameters["scope"] = scope.encodeWith(BotCommandScope.serializer())
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to get the current list of the bot's commands for the given scope and user language. Returns an Array of BotCommand objects. If commands aren't set, an empty list is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#getmycommands)
 * @param scope A JSON-serialized object, describing scope of users. Defaults to BotCommandScopeDefault.
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 * @returns [Array of BotCommand]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun getMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    GetMyCommandsAction(scope, languageCode)
