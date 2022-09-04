package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.MaskPosition
import eu.vendeli.tgbot.types.StickerType
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod

class CreateNewStickerSetAction(
    name: String,
    title: String,
    emojis: String,
    private val sticker: StickerFile,
    maskPosition: MaskPosition? = null,
    stickerType: StickerType = StickerType.Regular,
) : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("createNewStickerSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val MediaAction<Boolean>.defaultType: MediaContentType
        get() = sticker.contentType
    override val MediaAction<Boolean>.media: ImplicitFile<*>
        get() = sticker.toImplicitFile()
    override val MediaAction<Boolean>.dataField: String
        get() = when (sticker) {
            is StickerFile.PNG -> "png_sticker"
            is StickerFile.TGS -> "tgs_sticker"
            is StickerFile.WEBM -> "webm_sticker"
        }


    init {
        parameters["title"] = title
        parameters["name"] = name
        parameters["emojis"] = emojis
        if (maskPosition != null) parameters["mask_position"] = maskPosition
        parameters["sticker_type"] = stickerType
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
