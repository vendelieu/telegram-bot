@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SetChatMemberTagAction(
    userId: Long,
    tag: String? = null,
) : Action<Boolean>() {
    @TgAPI.Name("setChatMemberTag")
    override val method = "setChatMemberTag"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        tag?.let { parameters["tag"] = it.toJsonElement() }
    }
}

/**
 * Use this method to set a tag for a regular member in a group or a supergroup. The bot must be an administrator in the chat for this to work and must have the can_manage_tags administrator right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setchatmembertag)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param userId Unique identifier of the target user
 * @param tag New tag for the member; 0-16 characters, emoji are not allowed
 * @returns [Boolean]
 */
@TgAPI
inline fun setChatMemberTag(userId: Long, tag: String? = null) =
    SetChatMemberTagAction(userId, tag)

@TgAPI
inline fun setChatMemberTag(user: User, tag: String? = null) =
    setChatMemberTag(user.id, tag)
