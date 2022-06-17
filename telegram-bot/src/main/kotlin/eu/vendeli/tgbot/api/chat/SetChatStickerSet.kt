package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class SetChatStickerSetAction(stickerSetName: String) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatStickerSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["sticker_set_name"] = stickerSetName
    }
}

fun setChatStickerSet(stickerSetName: String) = SetChatStickerSetAction(stickerSetName)
