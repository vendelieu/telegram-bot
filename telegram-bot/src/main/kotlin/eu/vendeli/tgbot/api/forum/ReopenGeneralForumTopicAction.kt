@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this method to reopen a closed 'General' topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work
 * and must have the can_manage_topics administrator rights.
 * Returns True on success.
 */
class ReopenGeneralForumTopicAction : Action<Boolean>() {
    override val method = TgMethod("reopenGeneralForumTopic")
    override val returnType = getReturnType()
}

/**
 * Use this method to reopen a closed 'General' topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work
 * and must have the can_manage_topics administrator rights.
 * Returns True on success.
 */
fun reopenGeneralForumTopic() = ReopenGeneralForumTopicAction()
