package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.interfaces.MultipleResponse
import kotlinx.serialization.Serializable

@Serializable
data class MessageId(val messageId: Long) : MultipleResponse
