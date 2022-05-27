package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class SetChatStickerSetAction(stickerSetName: String) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatStickerSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["sticker_set_name"] = stickerSetName
    }
}

fun setChatStickerSet(stickerSetName: String) = SetChatStickerSetAction(stickerSetName)
