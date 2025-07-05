@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import kotlinx.serialization.builtins.serializer
import kotlin.jvm.JvmName

@TgAPI
class GetCustomEmojiStickersAction(
    customEmojiIds: List<String>,
) : SimpleAction<List<Sticker>>() {
    @TgAPI.Name("getCustomEmojiStickers")
    override val method = "getCustomEmojiStickers"
    override val returnType = getReturnType()

    init {
        parameters["custom_emoji_ids"] = customEmojiIds.encodeWith(String.serializer())
    }
}

/**
 * Use this method to get information about custom emoji stickers by their identifiers. Returns an Array of Sticker objects.
 *
 * [Api reference](https://core.telegram.org/bots/api#getcustomemojistickers)
 * @param customEmojiIds A JSON-serialized list of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
 * @returns [Array of Sticker]
 */
@TgAPI
inline fun getCustomEmojiStickers(customEmojiIds: List<String>) = GetCustomEmojiStickersAction(customEmojiIds)

@JvmName("getCustomEmojiStickersWithVararg")
@TgAPI
inline fun getCustomEmojiStickers(vararg customEmojiId: String) = getCustomEmojiStickers(customEmojiId.asList())
