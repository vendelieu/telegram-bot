@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class DeleteAllMessageReactionsAction(
    userId: Long? = null,
    actorChatId: Long? = null,
) : Action<Boolean>() {
    @TgAPI.Name("deleteAllMessageReactions")
    override val method = "deleteAllMessageReactions"
    override val returnType = getReturnType()

    init {
        if (userId != null) parameters["user_id"] = userId.toJsonElement()
        if (actorChatId != null) parameters["actor_chat_id"] = actorChatId.toJsonElement()
    }
}

/**
 * Use this method to delete all reactions added by a user or a chat from messages in a chat. The bot must be an administrator in the chat with the can_delete_messages right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deleteallmessagereactions)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @username)
 * @param userId Identifier of the user whose reactions will be removed, if the reactions were added by a user
 * @param actorChatId Identifier of the chat whose reactions will be removed, if the reactions were added by a chat
 * @returns [Boolean]
 */
@TgAPI
inline fun deleteAllMessageReactions(
    userId: Long? = null,
    actorChatId: Long? = null,
) = DeleteAllMessageReactionsAction(userId, actorChatId)
