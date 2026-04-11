@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetManagedBotTokenAction(
    userId: Long,
) : SimpleAction<String>() {
    @TgAPI.Name("getManagedBotToken")
    override val method = "getManagedBotToken"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Use this method to get the token of a managed bot. Returns the token as String on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getmanagedbottoken)
 * @param userId User identifier of the managed bot whose token will be returned
 * @returns [String]
 */
@TgAPI
inline fun getManagedBotToken(userId: Long) = GetManagedBotTokenAction(userId)
