package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.MaskPosition
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import kotlin.collections.set

class AddStickerToSetAction(
    name: String,
    emojis: String,
    private val sticker: StickerFile,
    maskPosition: MaskPosition? = null,
) : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("addStickerToSet")
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
        parameters["name"] = name
        parameters["emojis"] = emojis
        if (maskPosition != null) parameters["mask_position"] = maskPosition
    }
}

fun addStickerToSet(
    name: String,
    emojis: String,
    sticker: StickerFile,
    maskPosition: MaskPosition? = null,
) = AddStickerToSetAction(name, emojis, sticker, maskPosition)
