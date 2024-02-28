@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class CloseForumTopicAction(messageThreadId: Int) : Action<Boolean>() {
    override val method = TgMethod("closeForumTopic")
    override val returnType = getReturnType()

    init {
        parameters["message_thread_id"] = messageThreadId.toJsonElement()
    }
}

/**
 * Use this method to close an open topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work and must have the canManageTopics administrator rights,
 * unless it is the creator of the topic. Returns True on success.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun closeForumTopic(messageThreadId: Int) = CloseForumTopicAction(messageThreadId)
