@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement
import eu.vendeli.tgbot.utils.toPartData
import kotlin.collections.set

class AddStickerToSetAction(
    name: String,
    input: InputSticker,
) : MediaAction<Boolean>() {
    override val method = TgMethod("addStickerToSet")
    override val returnType = getReturnType()
    override val idRefField = "user_id"

    init {
        parameters["name"] = name.toJsonElement()
        parameters["sticker"] = input.also {
            if (it.sticker is ImplicitFile.InpFile) {
                val inpSticker = it.sticker as ImplicitFile.InpFile
                multipartData += inpSticker.file.toPartData(inpSticker.file.fileName)

                it.sticker = "attach://${inpSticker.file.fileName}".toImplicitFile()
            }
        }.encodeWith(InputSticker.serializer())
    }
}

/**
 * Use this method to add a new sticker to a set created by the bot. Emoji sticker sets can have up to 200 stickers. Other sticker sets can have up to 120 stickers. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#addstickertoset)
 * @param userId User identifier of sticker set owner
 * @param name Sticker set name
 * @param sticker A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been added to the set, then the set isn't changed.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun addStickerToSet(name: String, input: InputSticker) = AddStickerToSetAction(name, input)

inline fun addStickerToSet(name: String, input: () -> InputSticker) = AddStickerToSetAction(name, input())
