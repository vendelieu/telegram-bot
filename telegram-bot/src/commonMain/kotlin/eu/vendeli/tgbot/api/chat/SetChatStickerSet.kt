@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetChatStickerSetAction(stickerSetName: String) : Action<Boolean>() {
    override val method = TgMethod("setChatStickerSet")
    override val returnType = getReturnType()

    init {
        parameters["sticker_set_name"] = stickerSetName.toJsonElement()
    }
}

/**
 * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
 * @param chatId Required 
 * @param stickerSetName Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#setchatstickerset
*/
@Suppress("NOTHING_TO_INLINE")
inline fun setChatStickerSet(stickerSetName: String) = SetChatStickerSetAction(stickerSetName)
