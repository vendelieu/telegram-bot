package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.implementations.TokenBucketLimiterImpl
import eu.vendeli.tgbot.interfaces.RateLimitMechanism

data class RateLimiterConfiguration(
    var limits: RateLimits = RateLimits(),
    var mechanism: RateLimitMechanism = TokenBucketLimiterImpl(),
) {
    var exceededAction: suspend (Long, TelegramBot) -> Unit = { telegramId: Long, bot: TelegramBot ->
        message("Request limit exceeded, try again later.")
            .send(telegramId, bot)
    }
}
