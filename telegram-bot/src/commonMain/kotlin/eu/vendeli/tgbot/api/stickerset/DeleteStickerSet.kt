@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class DeleteStickerSetAction(
    name: String,
) : SimpleAction<Boolean>() {
    override val method = "deleteStickerSet"
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
@TgAPI
inline fun deleteStickerSet(name: String) = DeleteStickerSetAction(name)
