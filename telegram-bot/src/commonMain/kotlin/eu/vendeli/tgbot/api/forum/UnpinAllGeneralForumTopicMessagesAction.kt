@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class UnpinAllGeneralForumTopicMessagesAction : Action<Boolean>() {
    override val method = TgMethod("unpinAllGeneralForumTopicMessages")
    override val returnType = getReturnType()
}

/**
 * Use this method to clear the list of pinned messages in a General forum topic. The bot must be an administrator in the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on success.
 * @param chatId Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#unpinallgeneralforumtopicmessages
*/
@Suppress("NOTHING_TO_INLINE")
inline fun unpinAllGeneralForumTopicMessages() = UnpinAllGeneralForumTopicMessagesAction()
