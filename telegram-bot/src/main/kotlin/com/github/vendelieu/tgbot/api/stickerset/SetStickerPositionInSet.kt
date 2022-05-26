package com.github.vendelieu.tgbot.api.stickerset

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.internal.TgMethod

class SetStickerPositionInSetAction(sticker: String, position: Int) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("setStickerPositionInSet")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["sticker"] = sticker
        parameters["position"] = position
    }
}

fun setStickerPositionInSet(sticker: String, position: Int) = SetStickerPositionInSetAction(sticker, position)
