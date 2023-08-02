@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this method to close an open 'General' topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work
 * and must have the can_manage_topics administrator rights.
 * Returns True on success.
 */
class CloseGeneralForumTopicAction : Action<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("closeGeneralForumTopic")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()
}

/**
 * Use this method to close an open 'General' topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work
 * and must have the can_manage_topics administrator rights.
 * Returns True on success.
 */
fun closeGeneralForumTopic() = CloseGeneralForumTopicAction()
