@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement
import eu.vendeli.tgbot.utils.internal.transform

@TgAPI
class ReplaceStickerInSetAction(
    userId: Long,
    name: String,
    oldSticker: String,
    sticker: InputSticker,
) : Action<Boolean>() {
    @TgAPI.Name("replaceStickerInSet")
    override val method = "replaceStickerInSet"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["name"] = name.toJsonElement()
        parameters["old_sticker"] = oldSticker.toJsonElement()
        parameters["sticker"] = sticker
            .also {
                it.sticker = it.sticker.transform(multipartData)
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
@TgAPI
inline fun replaceStickerInSet(
    userId: Long,
    name: String,
    oldSticker: String,
    sticker: InputSticker,
) = ReplaceStickerInSetAction(userId, name, oldSticker, sticker)
