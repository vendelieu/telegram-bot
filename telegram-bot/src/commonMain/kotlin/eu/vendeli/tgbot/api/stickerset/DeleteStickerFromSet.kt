@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class DeleteStickerFromSetAction(
    sticker: String,
) : SimpleAction<Boolean>() {
    @TgAPI.Method("deleteStickerFromSet")
    override val method = "deleteStickerFromSet"
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker.toJsonElement()
    }
}

/**
 * Use this method to delete a sticker from a set created by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deletestickerfromset)
 * @param sticker File identifier of the sticker
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun deleteStickerFromSet(sticker: String) = DeleteStickerFromSetAction(sticker)
