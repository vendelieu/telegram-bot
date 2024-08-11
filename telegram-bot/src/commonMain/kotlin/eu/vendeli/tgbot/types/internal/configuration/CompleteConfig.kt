package eu.vendeli.tgbot.types.internal.configuration

import kotlinx.serialization.Serializable

@Serializable
data class CompleteConfig(
    val token: String,
    val pckg: String? = null,
    val configuration: BotConfiguration
)
