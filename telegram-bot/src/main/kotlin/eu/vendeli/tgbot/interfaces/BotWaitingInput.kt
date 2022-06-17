package eu.vendeli.tgbot.interfaces

/**
 * Bot waiting input, see [Waiting Input example in article](https://github.com/vendelieu/telegram-bot/wiki/Updates-handling#input-waiting)
 *
 */
interface BotWaitingInput {
    /**
     * Set new waiting point
     *
     * @param telegramId
     * @param identifier
     */
    fun set(telegramId: Long, identifier: String)

    /**
     * Get current waiting input of user.
     *
     * @param telegramId
     */
    fun get(telegramId: Long): String?

    /**
     * Delete current waiting input of user.
     *
     * @param telegramId
     */
    fun del(telegramId: Long)
}
