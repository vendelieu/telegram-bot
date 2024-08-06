package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.implementations.TokenBucketLimiterImpl
import eu.vendeli.tgbot.interfaces.helper.RateLimitMechanism

/**
 * Class containing configuration for global rate limiting.
 *
 * @property limits Global rate limits.
 * @property mechanism Mechanism used for rate limiting.
 * Default is [TokenBucket](https://en.wikipedia.org/wiki/Token_bucket) algorithm.
 * @property exceededAction Action that will be applied when the limit is exceeded.
 */
data class RateLimiterConfiguration(
    var limits: RateLimits = RateLimits(),
    var mechanism: RateLimitMechanism = TokenBucketLimiterImpl(),
) {
    var exceededAction: suspend (Long, TelegramBot) -> Unit = { telegramId: Long, bot: TelegramBot ->
        message("Request limit exceeded, try again later.")
            .send(telegramId, bot)
    }
}
