@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class DeleteMyCommandsAction(
    scope: BotCommandScope? = null,
    languageCode: String? = null,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("deleteMyCommands")
    override val returnType = getReturnType()

    init {
        if (scope != null) parameters["scope"] = scope.encodeWith(BotCommandScope.serializer())
        if (languageCode != null) parameters["language_code"] = languageCode.toJsonElement()
    }
}

/**
 * Use this method to delete the list of the bot's commands for the given scope and user language. After deletion, higher level commands will be shown to affected users. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deletemycommands)
 * @param scope A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to BotCommandScopeDefault.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun deleteMyCommands(languageCode: String? = null, scope: BotCommandScope? = null) =
    DeleteMyCommandsAction(scope, languageCode)
