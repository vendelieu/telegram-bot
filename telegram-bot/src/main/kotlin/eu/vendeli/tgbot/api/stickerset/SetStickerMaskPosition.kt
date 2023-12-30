@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.MaskPosition
import eu.vendeli.tgbot.utils.getReturnType

class SetStickerMaskPositionAction(
    sticker: String,
    maskPosition: MaskPosition? = null,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setStickerMaskPosition")
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker
        if (maskPosition != null) parameters["mask_position"] = maskPosition
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setStickerMaskPosition(sticker: String, maskPosition: MaskPosition? = null) =
    SetStickerMaskPositionAction(sticker, maskPosition)
