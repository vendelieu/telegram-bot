@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetStickerEmojiListAction(
    sticker: String,
    emojiList: List<String>,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("setStickerEmojiList")
    override val returnType = getReturnType()

    init {
        parameters["sticker"] = sticker.toJsonElement()
        parameters["emoji_list"] = emojiList.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setStickerEmojiList(sticker: String, emojiList: List<String>) =
    SetStickerEmojiListAction(sticker, emojiList)
