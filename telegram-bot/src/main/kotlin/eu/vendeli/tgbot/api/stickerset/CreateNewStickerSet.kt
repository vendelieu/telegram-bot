@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.builders.CreateNewStickerSetData
import eu.vendeli.tgbot.utils.getReturnType

class CreateNewStickerSetAction(
    private val data: CreateNewStickerSetData,
) : MediaAction<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("createNewStickerSet")
    override val returnType = getReturnType()

    override val MediaAction<Boolean>.defaultType: MediaContentType
        get() = data.sticker.contentType
    override val MediaAction<Boolean>.media: ImplicitFile<*>
        get() = data.sticker.toImplicitFile()
    override val MediaAction<Boolean>.dataField: String
        get() = "stickers"

    init {
        data.checkIsAllFieldsPresent()

        parameters["title"] = data.title
        parameters["name"] = data.name
        parameters["emojis"] = data.emojis
        parameters["sticker_format"] = data.sticker.stickerFormat
        if (data.maskPosition != null) parameters["mask_position"] = data.maskPosition
        if (data.stickerType != null) parameters["sticker_type"] = data.stickerType
        if (data.needsRepainting != null) parameters["needs_repainting"] = data.needsRepainting
    }
}

fun createNewStickerSet(block: CreateNewStickerSetData.() -> Unit) =
    CreateNewStickerSetAction(CreateNewStickerSetData().apply(block))

fun createNewStickerSet(data: CreateNewStickerSetData) = CreateNewStickerSetAction(data)
