package eu.vendeli.tgbot.types.session

import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.chatOrNull
import eu.vendeli.tgbot.types.component.userOrNull

/**
 * Resolves a [SessionKey] from a [ProcessedUpdate].
 *
 * Implementations must return `null` when no meaningful session can be derived
 * (e.g. updates without a chat, such as pure inline queries).
 */
fun interface SessionKeyStrategy {
    fun resolve(update: ProcessedUpdate): SessionKey?

    companion object {
        /** Always `ChatUser(chat.id, user.id)` when both are present, else falls back to `Chat(chat.id)`. */
        val ChatUser: SessionKeyStrategy = SessionKeyStrategy { update ->
            val chat = update.chatOrNull ?: return@SessionKeyStrategy null
            val user = update.userOrNull
            if (user != null) SessionKey.ChatUser(chat.id, user.id) else SessionKey.Chat(chat.id)
        }

        /** Always `Chat(chat.id)`. Useful for broadcast-style bots and channels. */
        val Chat: SessionKeyStrategy = SessionKeyStrategy { update ->
            update.chatOrNull?.let { SessionKey.Chat(it.id) }
        }

        /** `Chat(chat.id)` in private chats, `ChatUser(chat.id, user.id)` elsewhere. */
        val Auto: SessionKeyStrategy = SessionKeyStrategy { update ->
            val chat = update.chatOrNull ?: return@SessionKeyStrategy null
            val user = update.userOrNull
            when {
                chat.type == ChatType.Private -> SessionKey.Chat(chat.id)
                user != null -> SessionKey.ChatUser(chat.id, user.id)
                else -> SessionKey.Chat(chat.id)
            }
        }
    }
}
