@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.serialization.builtins.serializer

@TgAPI
class SetStickerEmojiListAction(
    sticker: String,
    emojiList: List<String>,
) : SimpleAction<Boolean>() {
    override val method = "setStickerEmojiList"
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker.toJsonElement()
        parameters["emoji_list"] = emojiList.encodeWith(String.serializer())
    }
}

/**
 * Use this method to change the list of emoji assigned to a regular or custom emoji sticker. The sticker must belong to a sticker set created by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setstickeremojilist)
 * @param sticker File identifier of the sticker
 * @param emojiList A JSON-serialized list of 1-20 emoji associated with the sticker
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setStickerEmojiList(sticker: String, emojiList: List<String>) =
    SetStickerEmojiListAction(sticker, emojiList)
