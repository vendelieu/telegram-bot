@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.media.StickerSet
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetStickerSetAction(
    name: String,
) : SimpleAction<StickerSet>() {
    @TgAPI.Name("getStickerSet")
    override val method = "getStickerSet"
    override val returnType = getReturnType()

    init {
        parameters["name"] = name.toJsonElement()
    }
}

/**
 * Use this method to get a sticker set. On success, a StickerSet object is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#getstickerset)
 * @param name Name of the sticker set
 * @returns [StickerSet]
 */
@TgAPI
inline fun getStickerSet(name: String) = GetStickerSetAction(name)
