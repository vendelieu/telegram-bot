package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.types.configuration.RateLimits

/**
 * Interface to implement features for request limits.
 */
fun interface RateLimitMechanism {
    /**
     * The function which will be called to check if the request limit is exceeded.
     *
     * @param limits A class with specified restriction parameters.
     * @param telegramId Telegram id of user.
     * @param actionId Parameter with an action identifier in case it is not a general restriction mechanism.
     * @return True if the action is to be restricted
     */
    suspend fun isLimited(limits: RateLimits, telegramId: Long, actionId: String?): Boolean
}
