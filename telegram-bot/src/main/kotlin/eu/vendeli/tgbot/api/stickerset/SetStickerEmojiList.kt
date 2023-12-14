@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetStickerEmojiListAction(
    sticker: String,
    emojiList: List<String>,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setStickerEmojiList")
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker
        parameters["emoji_list"] = emojiList
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setStickerEmojiList(sticker: String, emojiList: List<String>) =
    SetStickerEmojiListAction(sticker, emojiList)
