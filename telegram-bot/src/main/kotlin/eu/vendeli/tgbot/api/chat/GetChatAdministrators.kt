@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getInnerType

class GetChatAdministratorsAction : Action<List<ChatMember>> {
    override val method: TgMethod = TgMethod("getChatAdministrators")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val wrappedDataType = getInnerType()
}

fun getChatAdministrators() = GetChatAdministratorsAction()
