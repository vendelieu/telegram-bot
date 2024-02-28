@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
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
        parameters["sticker"] = input.encodeWith(InputSticker.serializer())
    }
}

/**
 * Use this method to add a new sticker to a set created by the bot. The format of the added sticker must match the format of the other stickers in the set. Emoji sticker sets can have up to 200 stickers. Animated and video sticker sets can have up to 50 stickers. Static sticker sets can have up to 120 stickers. Returns True on success.
 * @param userId Required 
 * @param name Required 
 * @param sticker Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#addstickertoset
*/
@Suppress("NOTHING_TO_INLINE")
inline fun addStickerToSet(name: String, input: InputSticker) = AddStickerToSetAction(name, input)

inline fun addStickerToSet(name: String, input: () -> InputSticker) = AddStickerToSetAction(name, input())
