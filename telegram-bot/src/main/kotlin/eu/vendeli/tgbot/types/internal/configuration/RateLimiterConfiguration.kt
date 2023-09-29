package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.interfaces.RateLimitMechanism

data class RateLimiterConfiguration(
    var mechanism: RateLimitMechanism,
    var limits: RateLimits = RateLimits(),
    var exceededAction: suspend (Long, TelegramBot) -> Unit = DEFAULT_RATELIMIT_EXCEED_ACTION,
)

private val DEFAULT_RATELIMIT_EXCEED_ACTION: suspend (Long, TelegramBot) -> Unit =
    { telegramId: Long, bot: TelegramBot ->
        message("Request limit exceeded, try again later.")
            .send(telegramId, bot)
    }

