@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeleteChatStickerSetAction : Action<Boolean>() {
    override val method = TgMethod("deleteChatStickerSet")
    override val returnType = getReturnType()
}

/**
 * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
 * Api reference: https://core.telegram.org/bots/api#deletechatstickerset
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @returns [Boolean]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun deleteChatStickerSet() = DeleteChatStickerSetAction()
