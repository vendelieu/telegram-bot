@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile

class SetChatPhotoAction(
    photo: ImplicitFile,
) : MediaAction<Boolean>() {
    override val method = TgMethod("setChatPhoto")
    override val returnType = getReturnType()

    init {
        handleImplicitFile(photo, "photo")
    }
}

/**
 * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setchatphoto)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param photo New chat photo, uploaded using multipart/form-data
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: ImplicitFile) = SetChatPhotoAction(file)
inline fun setChatPhoto(block: () -> String) = setChatPhoto(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPhoto(file: InputFile) = setChatPhoto(file.toImplicitFile())
