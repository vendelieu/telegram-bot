@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement
import eu.vendeli.tgbot.utils.internal.transform
import kotlin.collections.set

@TgAPI
class AddStickerToSetAction(
    userId: Long,
    name: String,
    sticker: InputSticker,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("addStickerToSet")
    override val method = "addStickerToSet"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["name"] = name.toJsonElement()
        parameters["sticker"] = sticker
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
@TgAPI
inline fun addStickerToSet(userId: Long, name: String, input: InputSticker) = AddStickerToSetAction(userId, name, input)

@TgAPI
inline fun addStickerToSet(userId: Long, name: String, input: () -> InputSticker) =
    addStickerToSet(userId, name, input())
