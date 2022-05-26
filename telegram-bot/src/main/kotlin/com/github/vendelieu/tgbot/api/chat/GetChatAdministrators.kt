package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.*
import com.github.vendelieu.tgbot.types.ChatMember
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetChatAdministratorsAction : Action<List<ChatMember>>, MultiResponseOf<ChatMember> {
    override val method: TgMethod = TgMethod("getChatAdministrators")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <T : MultipleResponse> TgAction.bunchResponseInnerType(): Class<T> = getInnerType() as Class<T>
}

fun getChatAdministrators() = GetChatAdministratorsAction()
