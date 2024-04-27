@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class DeleteStickerSetAction(name: String) : SimpleAction<Boolean>() {
    override val method = TgMethod("deleteStickerSet")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name.toJsonElement()
    }
}

/**
 * Use this method to delete a sticker set that was created by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deletestickerset)
 * @param name Sticker set name
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun deleteStickerSet(name: String) = DeleteStickerSetAction(name)
