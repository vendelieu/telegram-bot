package com.github.vendelieu.tgbot.api.stickerset

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.StickerSet
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetStickerSetAction(name: String) : SimpleAction<StickerSet> {
    override val method: TgMethod = TgMethod("getStickerSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["name"] = name
    }
}

fun getStickerSet(name: String) = GetStickerSetAction(name)
