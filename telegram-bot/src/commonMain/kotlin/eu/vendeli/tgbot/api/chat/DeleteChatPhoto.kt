@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class DeleteChatPhotoAction : Action<Boolean>() {
    @TgAPI.Name("deleteChatPhoto")
    override val method = "deleteChatPhoto"
    override val returnType = getReturnType()
}

/**
 * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deletechatphoto)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @returns [Boolean]
 */
@TgAPI
inline fun deleteChatPhoto() = DeleteChatPhotoAction()
