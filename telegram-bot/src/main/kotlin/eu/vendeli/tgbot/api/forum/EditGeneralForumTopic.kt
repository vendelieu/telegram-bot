@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this method to edit the name of the 'General' topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights.
 * Returns True on success.
 */
class EditGeneralForumTopicAction(name: String) : Action<Boolean>() {
    override val method = TgMethod("editGeneralForumTopic")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name
    }
}

/**
 * Use this method to edit the name of the 'General' topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights.
 * Returns True on success.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun editGeneralForumTopic(name: String) = EditGeneralForumTopicAction(name)
