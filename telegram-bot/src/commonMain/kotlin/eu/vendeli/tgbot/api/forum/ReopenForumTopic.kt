@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class ReopenForumTopicAction(messageThreadId: Int) : Action<Boolean>() {
    override val method = TgMethod("reopenForumTopic")
    override val returnType = getReturnType()

    init {
        parameters["message_thread_id"] = messageThreadId.toJsonElement()
    }
}

/**
 * Use this method to reopen a closed topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
 * @param chatId Required 
 * @param messageThreadId Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#reopenforumtopic
*/
@Suppress("NOTHING_TO_INLINE")
inline fun reopenForumTopic(messageThreadId: Int) = ReopenForumTopicAction(messageThreadId)
