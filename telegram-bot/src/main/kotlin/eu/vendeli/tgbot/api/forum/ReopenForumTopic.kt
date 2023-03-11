@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this method to reopen closed topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work and must have the canManageTopics administrator rights,
 * unless it is the creator of the topic. Returns True on success.
 */
class ReopenForumTopicAction(messageThreadId: Int) : SimpleAction<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("reopenForumTopic")
    override val returnType = getReturnType()

    init {
        parameters["message_thread_id"] = messageThreadId
    }
}

/**
 * Use this method to reopen closed topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work and must have the canManageTopics administrator rights,
 * unless it is the creator of the topic. Returns True on success.
 */
fun reopenForumTopic(messageThreadId: Int) = ReopenForumTopicAction(messageThreadId)
