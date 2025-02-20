package eu.vendeli.tgbot.types.component

import kotlinx.serialization.Serializable

@Serializable
enum class ParseMode {
    Markdown,
    MarkdownV2,
    HTML,
}
