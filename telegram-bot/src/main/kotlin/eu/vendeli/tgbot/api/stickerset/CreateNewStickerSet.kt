package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.MaskPosition
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod

@Suppress("UNUSED_PARAMETER")
class CreateNewStickerSetAction(
    name: String,
    title: String,
    emojis: String,
    sticker: StickerFile,
    containsMasks: Boolean? = null,
    maskPosition: MaskPosition? = null,
) : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("createNewStickerSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["title"] = title
        parameters["name"] = name
        parameters["emojis"] = emojis
        if (containsMasks != null) parameters["contains_masks"] = containsMasks
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

fun createNewStickerSet(
    name: String,
    title: String,
    emojis: String,
    sticker: StickerFile,
    containsMasks: Boolean? = null,
    maskPosition: MaskPosition? = null,
) = CreateNewStickerSetAction(name, title, emojis, sticker, containsMasks, maskPosition)
