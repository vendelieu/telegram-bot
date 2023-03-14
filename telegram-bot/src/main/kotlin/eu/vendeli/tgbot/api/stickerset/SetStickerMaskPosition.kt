@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.MaskPosition
import eu.vendeli.tgbot.utils.getReturnType

class SetStickerMaskPositionAction(
    sticker: String,
    maskPosition: MaskPosition?,
) : SimpleAction<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("setStickerMaskPosition")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()

    init {
        parameters["sticker"] = sticker
        if (maskPosition != null) parameters["mask_position"] = maskPosition
    }
}

fun setStickerMaskPosition(sticker: String, maskPosition: MaskPosition? = null) =
    SetStickerMaskPositionAction(sticker, maskPosition)
