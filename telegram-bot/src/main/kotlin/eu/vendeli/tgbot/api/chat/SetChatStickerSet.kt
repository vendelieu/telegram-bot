@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetChatStickerSetAction(stickerSetName: String) : Action<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("setChatStickerSet")
    override val returnType = getReturnType()

    init {
        parameters["sticker_set_name"] = stickerSetName
    }
}

fun setChatStickerSet(stickerSetName: String) = SetChatStickerSetAction(stickerSetName)
