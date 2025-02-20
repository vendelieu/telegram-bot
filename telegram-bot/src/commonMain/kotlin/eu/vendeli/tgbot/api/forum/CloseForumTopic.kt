@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class CloseForumTopicAction(
    messageThreadId: Int,
) : Action<Boolean>() {
    @TgAPI.Name("closeForumTopic")
    override val method = "closeForumTopic"
    override val returnType = getReturnType()

    init {
        parameters["message_thread_id"] = messageThreadId.toJsonElement()
    }
}

/**
 * Use this method to close an open topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#closeforumtopic)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun closeForumTopic(messageThreadId: Int) = CloseForumTopicAction(messageThreadId)
