@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetStickerPositionInSetAction(sticker: String, position: Int) : SimpleAction<Boolean>() {
    override val method = TgMethod("setStickerPositionInSet")
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker
        parameters["position"] = position
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setStickerPositionInSet(sticker: String, position: Int) = SetStickerPositionInSetAction(sticker, position)
