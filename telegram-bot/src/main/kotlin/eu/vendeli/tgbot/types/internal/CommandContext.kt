package eu.vendeli.tgbot.types.internal

data class CommandContext<out T : ProcessedUpdate>(
    val update: T,
    val parameters: Map<String, String>,
)
