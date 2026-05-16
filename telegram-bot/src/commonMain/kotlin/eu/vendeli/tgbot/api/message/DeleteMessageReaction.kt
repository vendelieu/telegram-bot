@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class DeleteMessageReactionAction(
    messageId: Long,
    userId: Long? = null,
    actorChatId: Long? = null,
) : Action<Boolean>() {
    @TgAPI.Name("deleteMessageReaction")
    override val method = "deleteMessageReaction"
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
        if (userId != null) parameters["user_id"] = userId.toJsonElement()
        if (actorChatId != null) parameters["actor_chat_id"] = actorChatId.toJsonElement()
    }
}

/**
 * Use this method to delete a reaction from a message. The bot must be an administrator in the chat with the can_delete_messages right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deletemessagereaction)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @username)
 * @param messageId Identifier of the target message
 * @param userId Identifier of the user whose reaction will be removed, if the reaction was added by a user
 * @param actorChatId Identifier of the chat whose reaction will be removed, if the reaction was added by a chat
 * @returns [Boolean]
 */
@TgAPI
inline fun deleteMessageReaction(
    messageId: Long,
    userId: Long? = null,
    actorChatId: Long? = null,
) = DeleteMessageReactionAction(messageId, userId, actorChatId)
