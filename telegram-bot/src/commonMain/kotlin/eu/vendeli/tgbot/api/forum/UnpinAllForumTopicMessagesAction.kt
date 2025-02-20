@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class UnpinAllForumTopicMessagesAction(
    messageThreadId: Int,
) : Action<Boolean>() {
    @TgAPI.Name("unpinAllForumTopicMessages")
    override val method = "unpinAllForumTopicMessages"
    override val returnType = getReturnType()

    init {
        parameters["message_thread_id"] = messageThreadId.toJsonElement()
    }
}

/**
 * Use this method to clear the list of pinned messages in a forum topic. The bot must be an administrator in the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#unpinallforumtopicmessages)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun unpinAllForumTopicMessages(messageThreadId: Int) = UnpinAllForumTopicMessagesAction(messageThreadId)
