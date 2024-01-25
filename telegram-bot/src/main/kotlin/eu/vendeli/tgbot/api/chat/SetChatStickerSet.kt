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

@Suppress("NOTHING_TO_INLINE")
inline fun setChatStickerSet(stickerSetName: String) = SetChatStickerSetAction(stickerSetName)
