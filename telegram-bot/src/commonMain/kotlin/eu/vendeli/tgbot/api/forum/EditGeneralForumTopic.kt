@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class EditGeneralForumTopicAction(name: String) : Action<Boolean>() {
    override val method = TgMethod("editGeneralForumTopic")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name.toJsonElement()
    }
}

/**
 * Use this method to edit the name of the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#editgeneralforumtopic)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param name New topic name, 1-128 characters
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun editGeneralForumTopic(name: String) = EditGeneralForumTopicAction(name)
