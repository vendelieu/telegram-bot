@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class DeleteForumTopicAction(
    messageThreadId: Int,
) : Action<Boolean>() {
    override val method = TgMethod("deleteForumTopic")
    override val returnType = getReturnType()

    init {
        parameters["message_thread_id"] = messageThreadId.toJsonElement()
    }
}

/**
 * Use this method to delete a forum topic along with all its messages in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_delete_messages administrator rights. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deleteforumtopic)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun deleteForumTopic(messageThreadId: Int) = DeleteForumTopicAction(messageThreadId)
