@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class UnpinAllForumTopicMessagesAction(messageThreadId: Int) : Action<Boolean>() {
    override val method = TgMethod("unpinAllForumTopicMessages")
    override val returnType = getReturnType()

    init {
        parameters["message_thread_id"] = messageThreadId.toJsonElement()
    }
}

/**
 * Use this method to clear the list of pinned messages in a forum topic. The bot must be an administrator in the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on success.
 * @param chatId Required 
 * @param messageThreadId Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#unpinallforumtopicmessages
*/
@Suppress("NOTHING_TO_INLINE")
inline fun unpinAllForumTopicMessages(messageThreadId: Int) = UnpinAllForumTopicMessagesAction(messageThreadId)
