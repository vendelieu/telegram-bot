@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class UnhideGeneralForumTopicAction : Action<Boolean>() {
    override val method = TgMethod("unhideGeneralForumTopic")
    override val returnType = getReturnType()
}

/**
 * Use this method to unhide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#unhidegeneralforumtopic)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun unhideGeneralForumTopic() = UnhideGeneralForumTopicAction()
