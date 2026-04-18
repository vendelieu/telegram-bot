package eu.vendeli.tgbot.interfaces.session

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.configuration.SessionConfiguration

/**
 * Produces the [SessionManager] used by a bot once `TelegramBot` has been constructed.
 *
 * The factory is invoked from `TelegramBot.init { }` so the freshly-built bot can be captured
 * — this is the piece that lets users swap [eu.vendeli.tgbot.implementations.DefaultSessionManager]
 * for a custom implementation (Redis, SQL, decorated manager, …) without giving up the bot
 * reference that sessions need to dispatch `deleteMessages` calls.
 *
 * Override via [SessionConfiguration.managerFactory]:
 *
 * ```
 * sessions {
 *     managerFactory = SessionManagerFactory { bot, _ -> MyRedisSessionManager(bot) }
 * }
 * ```
 */
fun interface SessionManagerFactory {
    fun create(bot: TelegramBot, config: SessionConfiguration): SessionManager
}
