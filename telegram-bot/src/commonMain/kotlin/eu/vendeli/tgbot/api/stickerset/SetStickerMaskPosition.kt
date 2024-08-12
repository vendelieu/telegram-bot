@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.media.MaskPosition
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SetStickerMaskPositionAction(
    sticker: String,
    maskPosition: MaskPosition? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Method("setStickerMaskPosition")
    override val method = "setStickerMaskPosition"
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker.toJsonElement()
        if (maskPosition != null) parameters["mask_position"] = maskPosition.encodeWith(MaskPosition.serializer())
    }
}

/**
 * Use this method to change the mask position of a mask sticker. The sticker must belong to a sticker set that was created by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setstickermaskposition)
 * @param sticker File identifier of the sticker
 * @param maskPosition A JSON-serialized object with the position where the mask should be placed on faces. Omit the parameter to remove the mask position.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setStickerMaskPosition(sticker: String, maskPosition: MaskPosition? = null) =
    SetStickerMaskPositionAction(sticker, maskPosition)
