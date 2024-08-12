@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SetChatAdministratorCustomTitleAction(
    userId: Long,
    customTitle: String,
) : Action<Boolean>() {
    override val method = "setChatAdministratorCustomTitle"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["custom_title"] = customTitle.toJsonElement()
    }
}

/**
 * Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setchatadministratorcustomtitle)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param userId Unique identifier of the target user
 * @param customTitle New custom title for the administrator; 0-16 characters, emoji are not allowed
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setChatAdministratorCustomTitle(userId: Long, customTitle: String) =
    SetChatAdministratorCustomTitleAction(userId, customTitle)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setChatAdministratorCustomTitle(user: User, customTitle: String) =
    setChatAdministratorCustomTitle(user.id, customTitle)
