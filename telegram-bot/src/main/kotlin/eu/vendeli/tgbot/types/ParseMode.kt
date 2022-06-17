package eu.vendeli.tgbot.types

enum class ParseMode {
    Markdown, MarkdownV2, HTML;

    override fun toString(): String = name
}
