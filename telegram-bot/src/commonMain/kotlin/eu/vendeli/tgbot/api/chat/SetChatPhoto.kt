@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile

class SetChatPhotoAction(photo: ImplicitFile) : MediaAction<Boolean>() {
    override val method = TgMethod("setChatPhoto")
    override val returnType = getReturnType()

    init {
        handleImplicitFile(photo, "photo")
    }
}

/**
 * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 * @param chatId Required 
 * @param photo Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#setchatphoto
*/
@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: ImplicitFile) = SetChatPhotoAction(file)
inline fun setChatPhoto(block: () -> String) = setChatPhoto(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: InputFile) = setChatPhoto(file.toImplicitFile())
