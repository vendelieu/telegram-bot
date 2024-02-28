@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeleteChatPhotoAction : Action<Boolean>() {
    override val method = TgMethod("deleteChatPhoto")
    override val returnType = getReturnType()
}

/**
 * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 * @param chatId Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#deletechatphoto
*/
@Suppress("NOTHING_TO_INLINE")
inline fun deleteChatPhoto() = DeleteChatPhotoAction()
