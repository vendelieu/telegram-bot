package eu.vendeli.tgbot.interfaces

interface BotChatData {
    fun set(telegramId: Long, key: String, value: Any?)
    fun get(telegramId: Long, key: String): Any?
    fun del(telegramId: Long, key: String)
    fun delPrevChatSection(telegramId: Long)
}
