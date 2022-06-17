package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class DeleteChatStickerSetAction : Action<Boolean> {
    override val method: TgMethod = TgMethod("deleteChatStickerSet")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun deleteChatStickerSet() = DeleteChatStickerSetAction()
