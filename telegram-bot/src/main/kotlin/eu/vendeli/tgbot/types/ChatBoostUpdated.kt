package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat

data class ChatBoostUpdated(
    val chat: Chat,
    val boost: ChatBoost,
)
