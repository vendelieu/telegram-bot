package com.github.vendelieu.tgbot.api.stickerset

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.internal.TgMethod

class DeleteStickerFromSetAction(sticker: String) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("deleteStickerFromSet")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["sticker"] = sticker
    }
}

fun deleteStickerFromSet(sticker: String) = DeleteStickerFromSetAction(sticker)
