package eu.vendeli.tgbot.types.internal

internal data class ParsedText(
    val command: String,
    val params: Map<String, String>,
)
