@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CreateNewStickerSetOptions
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.types.media.StickerFormat
import eu.vendeli.tgbot.utils.getReturnType

class CreateNewStickerSetAction(
    name: String,
    title: String,
    stickerFormat: StickerFormat,
    stickers: List<InputSticker>,
) : MediaAction<Boolean>(), OptionsFeature<CreateNewStickerSetAction, CreateNewStickerSetOptions> {
    override val method = TgMethod("createNewStickerSet")
    override val returnType = getReturnType()
    override val OptionsFeature<CreateNewStickerSetAction, CreateNewStickerSetOptions>.options:
        CreateNewStickerSetOptions
        get() = CreateNewStickerSetOptions()
    override val idRefField = "user_id"

    init {
        parameters["name"] = name
        parameters["title"] = title
        parameters["sticker_format"] = stickerFormat
        parameters["stickers"] = stickers
    }
}

fun createNewStickerSet(name: String, title: String, stickerFormat: StickerFormat, stickers: List<InputSticker>) =
    CreateNewStickerSetAction(name, title, stickerFormat, stickers)
