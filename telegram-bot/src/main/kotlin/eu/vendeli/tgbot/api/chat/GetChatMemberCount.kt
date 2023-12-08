@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatMemberCountAction : Action<Int>() {
    override val method = TgMethod("getChatMemberCount")
    override val returnType = getReturnType()
}

@Suppress("NOTHING_TO_INLINE")
inline fun getChatMemberCount() = GetChatMemberCountAction()
