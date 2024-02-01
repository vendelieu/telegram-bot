@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeleteChatStickerSetAction : Action<Boolean>() {
    override val method = TgMethod("deleteChatStickerSet")
    override val returnType = getReturnType()
}

inline fun deleteChatStickerSet() = DeleteChatStickerSetAction()
