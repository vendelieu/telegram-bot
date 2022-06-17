package eu.vendeli.tgbot.interfaces

/**
 * Bot chat data, see [Bot context article](https://github.com/vendelieu/telegram-bot/wiki/Bot-Context)
 */
interface BotChatData {
    /**
     * Set new ChatData value
     *
     * @param telegramId
     * @param key
     * @param value
     */
    fun set(telegramId: Long, key: String, value: Any?)

    /**
     * Get ChatData value
     *
     * @param telegramId
     * @param key
     */
    fun get(telegramId: Long, key: String): Any?

    /**
     * Del
     *
     * @param telegramId
     * @param key
     */
    fun del(telegramId: Long, key: String)

    /**
     * Del previous chat section data, is used to bind data to a class.
     *
     * @param telegramId
     */
    fun delPrevChatSection(telegramId: Long)
}
