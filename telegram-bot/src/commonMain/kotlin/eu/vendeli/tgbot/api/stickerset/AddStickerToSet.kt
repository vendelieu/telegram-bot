@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import eu.vendeli.tgbot.utils.transform
import kotlin.collections.set

class AddStickerToSetAction(
    userId: Long,
    name: String,
    input: InputSticker,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("addStickerToSet")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["name"] = name.toJsonElement()
        parameters["sticker"] = input
            .also {
                it.sticker = it.sticker.transform(multipartData)
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
inline fun addStickerToSet(userId: Long, name: String, input: InputSticker) = AddStickerToSetAction(userId, name, input)

inline fun addStickerToSet(userId: Long, name: String, input: () -> InputSticker) =
    addStickerToSet(userId, name, input())
