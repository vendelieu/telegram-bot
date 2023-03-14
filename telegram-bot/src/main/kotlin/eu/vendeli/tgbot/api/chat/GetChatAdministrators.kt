@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.chat.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getInnerType
import eu.vendeli.tgbot.utils.getReturnType

class GetChatAdministratorsAction : Action<List<ChatMember>>, ActionState() {
    override val TgAction<List<ChatMember>>.method: TgMethod
        get() = TgMethod("getChatAdministrators")
    override val TgAction<List<ChatMember>>.returnType: Class<List<ChatMember>>
        get() = getReturnType()
    override val TgAction<List<ChatMember>>.wrappedDataType: Class<ChatMember>
        get() = getInnerType()
}

fun getChatAdministrators() = GetChatAdministratorsAction()
