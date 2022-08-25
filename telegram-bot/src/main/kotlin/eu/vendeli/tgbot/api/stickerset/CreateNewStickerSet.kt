package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.MaskPosition
import eu.vendeli.tgbot.types.StickerType
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod

class CreateNewStickerSetAction(
    name: String,
    title: String,
    emojis: String,
    sticker: StickerFile,
    maskPosition: MaskPosition? = null,
    stickerType: StickerType = StickerType.Regular,
) : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("createNewStickerSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["title"] = title
        parameters["name"] = name
        parameters["emojis"] = emojis
        if (maskPosition != null) parameters["mask_position"] = maskPosition
        parameters["sticker_type"] = stickerType

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
    maskPosition: MaskPosition? = null,
    stickerType: StickerType = StickerType.Regular,
) = CreateNewStickerSetAction(name, title, emojis, sticker, maskPosition, stickerType)
