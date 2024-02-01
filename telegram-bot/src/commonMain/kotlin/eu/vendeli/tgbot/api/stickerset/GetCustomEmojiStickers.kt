@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to get information about custom emoji stickers by their identifiers
 *
 * @param customEmojiIds List of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
 */
class GetCustomEmojiStickersAction(customEmojiIds: List<String>) : SimpleAction<List<Sticker>>() {
    override val method = TgMethod("getCustomEmojiStickers")
    override val returnType = getReturnType()

    init {
        parameters["custom_emoji_ids"] = customEmojiIds.encodeWith(String.serializer())
    }
}

/**
 * Use this method to get information about custom emoji stickers by their identifiers
 *
 * @param customEmojiIds List of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
 */

inline fun getCustomEmojiStickers(customEmojiIds: List<String>) = GetCustomEmojiStickersAction(customEmojiIds)

@JvmName("getCustomEmojiStickersWithVararg")
inline fun getCustomEmojiStickers(vararg customEmojiId: String) = getCustomEmojiStickers(customEmojiId.asList())
