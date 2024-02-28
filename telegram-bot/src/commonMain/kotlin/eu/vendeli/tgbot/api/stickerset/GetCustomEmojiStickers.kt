@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import kotlinx.serialization.builtins.serializer
import kotlin.jvm.JvmName

class GetCustomEmojiStickersAction(customEmojiIds: List<String>) : SimpleAction<List<Sticker>>() {
    override val method = TgMethod("getCustomEmojiStickers")
    override val returnType = getReturnType()

    init {
        parameters["custom_emoji_ids"] = customEmojiIds.encodeWith(String.serializer())
    }
}

/**
 * Use this method to get information about custom emoji stickers by their identifiers. Returns an Array of Sticker objects.
 * @param customEmojiIds Required 
 * @returns [Array of Sticker]
 * Api reference: https://core.telegram.org/bots/api#getcustomemojistickers
*/
@Suppress("NOTHING_TO_INLINE")
inline fun getCustomEmojiStickers(customEmojiIds: List<String>) = GetCustomEmojiStickersAction(customEmojiIds)

@Suppress("NOTHING_TO_INLINE")
@JvmName("getCustomEmojiStickersWithVararg")
inline fun getCustomEmojiStickers(vararg customEmojiId: String) = getCustomEmojiStickers(customEmojiId.asList())
