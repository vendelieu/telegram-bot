package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod

class SetStickerPositionInSetAction(sticker: String, position: Int) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("setStickerPositionInSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["sticker"] = sticker
        parameters["position"] = position
    }
}

fun setStickerPositionInSet(sticker: String, position: Int) = SetStickerPositionInSetAction(sticker, position)
