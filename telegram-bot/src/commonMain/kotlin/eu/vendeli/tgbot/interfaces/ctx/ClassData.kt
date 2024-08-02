package eu.vendeli.tgbot.interfaces.ctx

/**
 * Bot class data, see [Bot context article](https://github.com/vendelieu/telegram-bot/wiki/Bot-Context)
 */
interface ClassData<T> : BotContext<T> {
    /**
     * Clear all class data entries.
     * Used to remove previous context data.
     *
     * @param telegramId
     */
    suspend fun clearAll(telegramId: Long)
}
