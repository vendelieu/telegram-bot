package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.*
import eu.vendeli.tgbot.types.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod

class GetChatAdministratorsAction : Action<List<ChatMember>>, MultiResponseOf<ChatMember> {
    override val method: TgMethod = TgMethod("getChatAdministrators")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <T : MultipleResponse> TgAction.bunchResponseInnerType(): Class<T> = getInnerType() as Class<T>
}

fun getChatAdministrators() = GetChatAdministratorsAction()
