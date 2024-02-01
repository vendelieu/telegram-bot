package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
enum class ParseMode {
    Markdown,
    MarkdownV2,
    HTML,
}
