package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.MaskPosition
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import kotlin.collections.MutableMap
import kotlin.collections.mutableMapOf
import kotlin.collections.set

@Suppress("UNUSED_PARAMETER")
class AddStickerToSetAction(
    name: String,
    emojis: String,
    sticker: StickerFile,
    maskPosition: MaskPosition? = null,
) : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("addStickerToSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["name"] = name
        parameters["emojis"] = emojis
        if (maskPosition != null) parameters["mask_position"] = maskPosition

        when (sticker) {
            is StickerFile.PNG -> setDataField("png_sticker")
            is StickerFile.TGS -> setDataField("tgs_sticker")
            is StickerFile.WEBM -> setDataField("webm_sticker")
        }
        setDefaultType(sticker.contentType)
        setMedia(sticker.file)
    }
}

fun addStickerToSet(
    name: String,
    emojis: String,
    sticker: StickerFile,
    maskPosition: MaskPosition? = null,
) = AddStickerToSetAction(name, emojis, sticker, maskPosition)
