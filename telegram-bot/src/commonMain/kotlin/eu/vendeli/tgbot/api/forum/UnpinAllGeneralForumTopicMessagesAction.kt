@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class UnpinAllGeneralForumTopicMessagesAction : Action<Boolean>() {
    @TgAPI.Name("unpinAllGeneralForumTopicMessages")
    override val method = "unpinAllGeneralForumTopicMessages"
    override val returnType = getReturnType()
}

/**
 * Use this method to clear the list of pinned messages in a General forum topic. The bot must be an administrator in the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#unpinallgeneralforumtopicmessages)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @returns [Boolean]
 */
@TgAPI
inline fun unpinAllGeneralForumTopicMessages() = UnpinAllGeneralForumTopicMessagesAction()
