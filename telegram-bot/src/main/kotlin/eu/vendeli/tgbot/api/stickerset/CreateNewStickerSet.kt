@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.builders.CreateNewStickerSetData
import eu.vendeli.tgbot.utils.getReturnType

class CreateNewStickerSetAction(
    private val data: CreateNewStickerSetData,
) : MediaAction<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("createNewStickerSet")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()
    override val MediaAction<Boolean>.isImplicit: Boolean
        get() = data.stickers.any { it.sticker.data is ImplicitFile.InpFile }
    private val defaultType = data.stickers.first().sticker.contentType

    init {
        val firstStickerFormat = data.stickers.first().sticker.stickerFormat
        require(data.stickers.all { it.sticker.stickerFormat == firstStickerFormat }) {
            "All stickers must be of the same type."
        }
        parameters["name"] = data.name
        parameters["title"] = data.title
        parameters["sticker_format"] = firstStickerFormat
        if (data.stickerType != null) parameters["sticker_type"] = data.stickerType
        if (data.needsRepainting != null) parameters["needs_repainting"] = data.needsRepainting

        parameters["stickers"] = if (!isImplicit) data.stickers
        else data.stickers.mapIndexed { index, inputSticker ->
            val defaultName = "sticker-$index.$defaultType"
            InputSticker(
                sticker = inputSticker.sticker.let { s ->
                    // if string keep it as is
                    if (s.data is ImplicitFile.Str) return@let s
                    val name = (s.data as ImplicitFile.InpFile).file.fileName.takeIf { it != "file" } ?: defaultName
                    // in other cases put file to parameters
                    parameters[name] = s.data.file.data

                    StickerFile.AttachedFile(
                        // and replace it with 'attach://$file' link
                        file = ImplicitFile.Str("attach://$name"),
                        format = s.stickerFormat,
                        contentType = s.contentType,
                    )
                },
                emojiList = inputSticker.emojiList,
                maskPosition = inputSticker.maskPosition,
                keywords = inputSticker.keywords,
            )
        }
    }
}

fun createNewStickerSet(block: CreateNewStickerSetData.() -> Unit) =
    CreateNewStickerSetAction(CreateNewStickerSetData("", "", listOf()).apply(block).validateFields())

fun createNewStickerSet(data: CreateNewStickerSetData) = CreateNewStickerSetAction(data)
