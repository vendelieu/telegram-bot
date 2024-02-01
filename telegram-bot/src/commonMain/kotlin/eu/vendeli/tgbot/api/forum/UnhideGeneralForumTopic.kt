@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this method to unhide 'General' topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work
 * and must have the can_manage_topics administrator rights.
 * Returns True on success.
 */
class UnhideGeneralForumTopicAction : Action<Boolean>() {
    override val method = TgMethod("unhideGeneralForumTopic")
    override val returnType = getReturnType()
}

/**
 * Use this method to unhide 'General' topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work
 * and must have the can_manage_topics administrator rights.
 * Returns True on success.
 */

inline fun unhideGeneralForumTopic() = UnhideGeneralForumTopicAction()
