@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this method to clear the list of pinned messages in a forum topic.
 * The bot must be an administrator in the chat for this to work and must have
 * the can_pin_messages administrator right in the supergroup.
 * Returns True on success.
 */
class UnpinAllForumTopicMessagesAction(messageThreadId: Int) : Action<Boolean>() {
    override val method = TgMethod("unpinAllForumTopicMessages")
    override val returnType = getReturnType()

    init {
        parameters["message_thread_id"] = messageThreadId
    }
}

/**
 * Use this method to clear the list of pinned messages in a forum topic.
 * The bot must be an administrator in the chat for this to work and must have
 * the can_pin_messages administrator right in the supergroup.
 * Returns True on success.
 */
fun unpinAllForumTopicMessages(messageThreadId: Int) = UnpinAllForumTopicMessagesAction(messageThreadId)
