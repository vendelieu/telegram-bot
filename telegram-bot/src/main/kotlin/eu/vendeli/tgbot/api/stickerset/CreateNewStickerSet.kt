@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.ImplicitFile.InpFile
import eu.vendeli.tgbot.types.internal.ImplicitFile.Str
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CreateNewStickerSetOptions
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.getReturnType

class CreateNewStickerSetAction(
    name: String,
    title: String,
    stickers: List<InputSticker>,
) : ActionState(), MediaAction<Boolean>, OptionsFeature<CreateNewStickerSetAction, CreateNewStickerSetOptions> {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("createNewStickerSet")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()
    override val MediaAction<Boolean>.isImplicit: Boolean
        get() = isInputFile
    override val OptionsFeature<CreateNewStickerSetAction, CreateNewStickerSetOptions>.options:
        CreateNewStickerSetOptions
            get() = CreateNewStickerSetOptions()

    private val isInputFile = stickers.any { it.sticker.data is InpFile }
    private val defaultType = stickers.first().sticker.contentType

    init {
        val firstStickerFormat = stickers.first().sticker.stickerFormat
        require(stickers.all { it.sticker.stickerFormat == firstStickerFormat }) {
            "All stickers must be of the same type."
        }
        parameters["name"] = name
        parameters["title"] = title
        parameters["sticker_format"] = firstStickerFormat

        parameters["stickers"] = if (!isImplicit) stickers
        else stickers.mapIndexed { index, inputSticker ->
            val defaultName = "sticker-$index.$defaultType"
            InputSticker(
                sticker = inputSticker.sticker.let { s ->
                    // if string keep it as is
                    if (s.data is Str) return@let s
                    val filename = (s.data as InpFile).file.fileName.takeIf { it != "file" } ?: defaultName
                    // in other cases put file to parameters
                    parameters[filename] = s.data

                    StickerFile.AttachedFile(
                        // and replace it with 'attach://$file' link
                        file = Str("attach://$filename"),
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

fun createNewStickerSet(name: String, title: String, stickers: List<InputSticker>) =
    CreateNewStickerSetAction(name, title, stickers)
