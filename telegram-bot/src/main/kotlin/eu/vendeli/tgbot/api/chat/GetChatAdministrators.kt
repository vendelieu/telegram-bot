@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.WrappedTypeOf
import eu.vendeli.tgbot.interfaces.getInnerType
import eu.vendeli.tgbot.types.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod

class GetChatAdministratorsAction : Action<List<ChatMember>>, WrappedTypeOf<ChatMember> {
    override val method: TgMethod = TgMethod("getChatAdministrators")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val wrappedDataType = getInnerType()
}

fun getChatAdministrators() = GetChatAdministratorsAction()
