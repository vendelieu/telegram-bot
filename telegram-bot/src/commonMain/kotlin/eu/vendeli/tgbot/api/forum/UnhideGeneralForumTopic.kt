@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class UnhideGeneralForumTopicAction : Action<Boolean>() {
    override val method = TgMethod("unhideGeneralForumTopic")
    override val returnType = getReturnType()
}

/**
 * Use this method to unhide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
 * @param chatId Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#unhidegeneralforumtopic
*/
@Suppress("NOTHING_TO_INLINE")
inline fun unhideGeneralForumTopic() = UnhideGeneralForumTopicAction()
