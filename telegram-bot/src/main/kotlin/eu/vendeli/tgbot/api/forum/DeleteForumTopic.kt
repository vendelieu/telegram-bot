@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod

/**
 * Use this method to delete a forum topic along with all its messages in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work
 * and must have the canDeleteMessages administrator rights.
 * Returns True on success.
 */
class DeleteForumTopicAction(messageThreadId: Int) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("deleteForumTopic")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["message_thread_id"] = messageThreadId
    }
}

/**
 * Use this method to delete a forum topic along with all its messages in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work
 * and must have the canDeleteMessages administrator rights.
 * Returns True on success.
 */
fun deleteForumTopic(messageThreadId: Int) = DeleteForumTopicAction(messageThreadId)
