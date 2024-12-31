package eu.vendeli.spring.starter

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.utils.BotConfigurator
import kotlinx.coroutines.delay

/**
 * Base configuration for a Telegram bot.
 *
 * @property autostartLongPolling Start long polling after the bot is created.
 * @property identifier An identifier for this bot, used to match configuration and bot instance,
 * empty list to receive all update types except chat_member, message_reaction, and message_reaction_count (default).
 * If not specified, the previous setting will be used.
 * @property allowedUpdates The types of updates to be received and processed.
 */
abstract class BotConfiguration {
    open val autostartLongPolling = true
    open val identifier: String = "KtGram"
    open val allowedUpdates: List<UpdateType>? = null

    /**
     * Specify the configuration that will be applied to the bot.
     */
    abstract fun applyCfg(): BotConfigurator

    /**
     * Called after the bot is created and before it starts.
     */
    open suspend fun onInit(bot: TelegramBot) {}

    /**
     * Called when an exception is thrown by a handler.
     * Handler is automatically restarts by default after this hook is called.
     * Default behavior is to delay for 3 seconds.
     */
    open suspend fun onHandlerException(exception: Throwable) {
        delay(3000L)
    }
}
