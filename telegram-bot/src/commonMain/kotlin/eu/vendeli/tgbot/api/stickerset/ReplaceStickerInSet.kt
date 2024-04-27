@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement
import eu.vendeli.tgbot.utils.toPartData

class ReplaceStickerInSetAction(
    userId: Long,
    name: String,
    oldSticker: String,
    sticker: InputSticker,
) : Action<Boolean>() {
    override val method = TgMethod("replaceStickerInSet")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["name"] = name.toJsonElement()
        parameters["old_sticker"] = oldSticker.toJsonElement()
        parameters["sticker"] = sticker.also {
            if (it.sticker is ImplicitFile.InpFile) {
                val inpSticker = it.sticker as ImplicitFile.InpFile
                multipartData += inpSticker.file.toPartData(inpSticker.file.fileName)

                it.sticker = "attach://${inpSticker.file.fileName}".toImplicitFile()
            }
        }.encodeWith(InputSticker.serializer())
    }
}

/**
 * Use this method to replace an existing sticker in a sticker set with a new one. The method is equivalent to calling deleteStickerFromSet, then addStickerToSet, then setStickerPositionInSet. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#replacestickerinset)
 * @param userId User identifier of the sticker set owner
 * @param name Sticker set name
 * @param oldSticker File identifier of the replaced sticker
 * @param sticker A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been added to the set, then the set remains unchanged.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun replaceStickerInSet(
    userId: Long,
    name: String,
    oldSticker: String,
    sticker: InputSticker,
) = ReplaceStickerInSetAction(userId, name, oldSticker, sticker)
