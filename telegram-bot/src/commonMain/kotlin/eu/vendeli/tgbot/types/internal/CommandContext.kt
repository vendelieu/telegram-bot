package eu.vendeli.tgbot.types.internal

import kotlinx.serialization.Serializable

@Serializable
data class CommandContext<out T : ProcessedUpdate>(
    val update: T,
    val parameters: Map<String, String>,
)
