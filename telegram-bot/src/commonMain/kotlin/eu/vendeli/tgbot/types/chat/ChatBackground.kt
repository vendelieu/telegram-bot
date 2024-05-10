package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.BackgroundType
import kotlinx.serialization.Serializable

@Serializable
data class ChatBackground(val type: BackgroundType)
