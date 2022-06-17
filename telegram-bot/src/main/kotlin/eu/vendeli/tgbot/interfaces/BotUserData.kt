package eu.vendeli.tgbot.interfaces

/**
 * Bot user data, see [Bot context article](https://github.com/vendelieu/telegram-bot/wiki/Bot-Context)
 */
interface BotUserData {
    /**
     * Set new UserData value
     *
     * @param telegramId
     * @param key
     * @param value
     */
    fun set(telegramId: Long, key: String, value: Any?)

    /**
     * Get UserData value
     *
     * @param telegramId
     * @param key
     */
    fun get(telegramId: Long, key: String): Any?

    /**
     * Del UserData value
     *
     * @param telegramId
     * @param key
     */
    fun del(telegramId: Long, key: String)
}
