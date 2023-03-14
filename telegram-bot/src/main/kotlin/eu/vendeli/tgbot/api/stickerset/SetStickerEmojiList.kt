@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetStickerEmojiListAction(
    sticker: String,
    emojiList: List<String>,
) : SimpleAction<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("setStickerEmojiList")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()

    init {
        parameters["sticker"] = sticker
        parameters["emoji_list"] = emojiList
    }
}

fun setStickerEmojiList(sticker: String, emojiList: List<String>) =
    SetStickerEmojiListAction(sticker, emojiList)
