@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getInnerType
import eu.vendeli.tgbot.utils.getReturnType

class GetChatAdministratorsAction : Action<List<ChatMember>>, ActionState() {
    override val method: TgMethod = TgMethod("getChatAdministrators")
    override val returnType = getReturnType()
    override val wrappedDataType = getInnerType()
}

fun getChatAdministrators() = GetChatAdministratorsAction()
