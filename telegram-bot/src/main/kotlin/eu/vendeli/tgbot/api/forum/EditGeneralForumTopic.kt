@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod

/**
 * Use this method to edit the name of the 'General' topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights.
 * Returns True on success.
 */
class EditGeneralForumTopicAction(name: String) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("editGeneralForumTopic")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["name"] = name
    }
}

/**
 * Use this method to edit the name of the 'General' topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights.
 * Returns True on success.
 */
fun editGeneralForumTopic(name: String) = EditGeneralForumTopicAction(name)
