@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class DeleteChatPhotoAction : Action<Boolean> {
    override val method: TgMethod = TgMethod("deleteChatPhoto")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun deleteChatPhoto() = DeleteChatPhotoAction()
