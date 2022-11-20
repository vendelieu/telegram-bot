package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.internal.RateLimits

/**
 * Interface to implement features for request limits.
 */
interface RateLimitMechanism {
    /**
     * The function that will be called when the limits are exceeded.
     *
     * @param telegramId Telegram id of user.
     * @param bot Bot instance.
     */
    suspend fun exceededLimitResponse(telegramId: Long, bot: TelegramBot) {
        message("Request limit exceeded, try again later.").send(telegramId, bot)
    }

    /**
     * The function which will be called to check if the request limit is exceeded.
     *
     * @param limits A class with specified restriction parameters.
     * @param telegramId Telegram id of user.
     * @param actionId Parameter with an action identifier in case it is not a general restriction mechanism.
     * @return True if the action is to be restricted
     */
    suspend fun isLimited(limits: RateLimits, telegramId: Long, actionId: String? = null): Boolean
}