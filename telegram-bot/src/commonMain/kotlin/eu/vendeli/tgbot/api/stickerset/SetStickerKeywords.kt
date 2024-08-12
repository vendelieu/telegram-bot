@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.serialization.builtins.serializer

@TgAPI
class SetStickerKeywordsAction(
    sticker: String,
    keywords: List<String>? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Method("setStickerKeywords")
    override val method = "setStickerKeywords"
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker.toJsonElement()
        if (keywords != null) parameters["keywords"] = keywords.encodeWith(String.serializer())
    }
}

/**
 * Use this method to change search keywords assigned to a regular or custom emoji sticker. The sticker must belong to a sticker set created by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setstickerkeywords)
 * @param sticker File identifier of the sticker
 * @param keywords A JSON-serialized list of 0-20 search keywords for the sticker with total length of up to 64 characters
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setStickerKeywords(sticker: String, keywords: List<String>? = null) =
    SetStickerKeywordsAction(sticker, keywords)
