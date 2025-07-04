@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class HideGeneralForumTopicAction : Action<Boolean>() {
    @TgAPI.Name("hideGeneralForumTopic")
    override val method = "hideGeneralForumTopic"
    override val returnType = getReturnType()
}

/**
 * Use this method to hide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. The topic will be automatically closed if it was open. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#hidegeneralforumtopic)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @returns [Boolean]
 */
@TgAPI
inline fun hideGeneralForumTopic() = HideGeneralForumTopicAction()
