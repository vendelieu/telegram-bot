package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class SentWebAppMessage(val inlineMessageId: String? = null)
