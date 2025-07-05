@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.MediaAction
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.component.InputFile
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.handleImplicitFile
import eu.vendeli.tgbot.utils.common.toImplicitFile

@TgAPI
class SetChatPhotoAction(
    photo: ImplicitFile,
) : MediaAction<Boolean>() {
    @TgAPI.Name("setChatPhoto")
    override val method = "setChatPhoto"
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
@TgAPI
inline fun setChatPhoto(file: ImplicitFile) = SetChatPhotoAction(file)

@TgAPI
inline fun setChatPhoto(block: () -> String) = setChatPhoto(block().toImplicitFile())

@TgAPI
inline fun setChatPhoto(file: InputFile) = setChatPhoto(file.toImplicitFile())
