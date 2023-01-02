@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod

/**
 * Use this method to clear the list of pinned messages in a forum topic.
 * The bot must be an administrator in the chat for this to work and must have
 * the can_pin_messages administrator right in the supergroup.
 * Returns True on success.
 */
class UnpinAllForumTopicMessagesAction(messageThreadId: Int) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("unpinAllForumTopicMessages")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

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
