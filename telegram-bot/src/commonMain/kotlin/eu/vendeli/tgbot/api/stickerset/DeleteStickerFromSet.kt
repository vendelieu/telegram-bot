@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class DeleteStickerFromSetAction(sticker: String) : SimpleAction<Boolean>() {
    override val method = TgMethod("deleteStickerFromSet")
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker.toJsonElement()
    }
}

/**
 * Use this method to delete a sticker from a set created by the bot. Returns True on success.
 * @param sticker Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#deletestickerfromset
*/
@Suppress("NOTHING_TO_INLINE")
inline fun deleteStickerFromSet(sticker: String) = DeleteStickerFromSetAction(sticker)
