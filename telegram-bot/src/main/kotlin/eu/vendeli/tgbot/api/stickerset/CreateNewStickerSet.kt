@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.builders.CreateNewStickerSetData

class CreateNewStickerSetAction(private val data: CreateNewStickerSetData) : MediaAction<Boolean> {
    override val method: TgMethod = TgMethod("createNewStickerSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val MediaAction<Boolean>.defaultType: MediaContentType
        get() = data.sticker.contentType
    override val MediaAction<Boolean>.media: ImplicitFile<*>
        get() = data.sticker.toImplicitFile()
    override val MediaAction<Boolean>.dataField: String
        get() = when (data.sticker) {
            is StickerFile.PNG -> "png_sticker"
            is StickerFile.TGS -> "tgs_sticker"
            is StickerFile.WEBM -> "webm_sticker"
        }

    init {
        data.checkIsAllFieldsPresent()

        parameters["title"] = data.title
        parameters["name"] = data.name
        parameters["emojis"] = data.emojis
        if (data.maskPosition != null) parameters["mask_position"] = data.maskPosition
        parameters["sticker_type"] = data.stickerType
    }
}

fun createNewStickerSet(block: CreateNewStickerSetData.() -> Unit) =
    CreateNewStickerSetAction(CreateNewStickerSetData().apply(block))

fun createNewStickerSet(data: CreateNewStickerSetData) = CreateNewStickerSetAction(data)
