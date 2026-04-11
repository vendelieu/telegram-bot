@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class ReplaceManagedBotTokenAction(
    userId: Long,
) : SimpleAction<String>() {
    @TgAPI.Name("replaceManagedBotToken")
    override val method = "replaceManagedBotToken"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Use this method to revoke the current token of a managed bot and generate a new one. Returns the new token as String on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#replacemanagedbottoken)
 * @param userId User identifier of the managed bot whose token will be replaced
 * @returns [String]
 */
@TgAPI
inline fun replaceManagedBotToken(userId: Long) = ReplaceManagedBotTokenAction(userId)
