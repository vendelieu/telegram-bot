@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CreateNewStickerSetOptions
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement
import eu.vendeli.tgbot.utils.toPartData

class CreateNewStickerSetAction(
    name: String,
    title: String,
    stickers: List<InputSticker>,
) : MediaAction<Boolean>(), OptionsFeature<CreateNewStickerSetAction, CreateNewStickerSetOptions> {
    override val method = TgMethod("createNewStickerSet")
    override val returnType = getReturnType()
    override val options = CreateNewStickerSetOptions()
    override val idRefField = "user_id"

    init {
        parameters["name"] = name.toJsonElement()
        parameters["title"] = title.toJsonElement()
        parameters["stickers"] = stickers.onEach {
            if (it.sticker is ImplicitFile.InpFile) {
                val sticker = it.sticker as ImplicitFile.InpFile
                multipartData += sticker.file.toPartData(sticker.file.fileName)

                it.sticker = "attach://${sticker.file.fileName}".toImplicitFile()
            }
        }.encodeWith(InputSticker.serializer())
    }
}

/**
 * Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus created. Returns True on success.
 * Api reference: https://core.telegram.org/bots/api#createnewstickerset
 * @param userId User identifier of created sticker set owner
 * @param name Short name of sticker set, to be used in t.me/addstickers/ URLs (e.g., animals). Can contain only English letters, digits and underscores. Must begin with a letter, can't contain consecutive underscores and must end in "_by_<bot_username>". <bot_username> is case insensitive. 1-64 characters.
 * @param title Sticker set title, 1-64 characters
 * @param stickers A JSON-serialized list of 1-50 initial stickers to be added to the sticker set
 * @param stickerFormat Format of stickers in the set, must be one of "static", "animated", "video"
 * @param stickerType Type of stickers in the set, pass "regular", "mask", or "custom_emoji". By default, a regular sticker set is created.
 * @param needsRepainting Pass True if stickers in the sticker set must be repainted to the color of text when used in messages, the accent color if used as emoji status, white on chat photos, or another appropriate color based on context; for custom emoji sticker sets only
 * @returns [Boolean]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun createNewStickerSet(
    name: String,
    title: String,
    stickers: List<InputSticker>,
) = CreateNewStickerSetAction(name, title, stickers)
