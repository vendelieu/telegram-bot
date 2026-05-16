@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetUserPersonalChatMessagesAction(
    userId: Long,
    limit: Int,
) : SimpleAction<List<Message>>() {
    @TgAPI.Name("getUserPersonalChatMessages")
    override val method = "getUserPersonalChatMessages"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["limit"] = limit.toJsonElement()
    }
}

/**
 * Use this method to fetch messages from a user's personal chat with the bot. Returns an Array of Message objects.
 *
 * [Api reference](https://core.telegram.org/bots/api#getuserpersonalchatmessages)
 * @param userId Unique identifier for the target user
 * @param limit The maximum number of messages to return; 1-20
 * @returns [Array of Message]
 */
@TgAPI
inline fun getUserPersonalChatMessages(userId: Long, limit: Int) =
    GetUserPersonalChatMessagesAction(userId, limit)
