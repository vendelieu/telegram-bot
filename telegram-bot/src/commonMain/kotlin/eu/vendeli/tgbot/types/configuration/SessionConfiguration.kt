package eu.vendeli.tgbot.types.configuration

import eu.vendeli.tgbot.annotations.dsl.ConfigurationDSL
import eu.vendeli.tgbot.implementations.DefaultSessionManager
import eu.vendeli.tgbot.implementations.InMemorySessionStorage
import eu.vendeli.tgbot.interfaces.session.SessionManager
import eu.vendeli.tgbot.interfaces.session.SessionManagerFactory
import eu.vendeli.tgbot.interfaces.session.SessionStorage
import eu.vendeli.tgbot.types.session.SessionKeyStrategy

/**
 * Configuration block for the [SessionManager] subsystem.
 *
 * The subsystem is always-on with sensible defaults; calling `sessions { }` in
 * [BotConfiguration] is only required to override [keyStrategy], [storage], or
 * [managerFactory]. Auto-tracking is predicate-driven — the interceptor only writes when a
 * session has been opened (via `bot.sessions.of(update)` / `bot.sessions.get(...)`), so
 * untouched bots pay no storage cost.
 *
 * @property keyStrategy How session keys are derived from a [eu.vendeli.tgbot.types.component.ProcessedUpdate].
 * @property storage Backend that stores tracked messages. Consumed by the default factory; custom factories may ignore it.
 * @property managerFactory Builds the [SessionManager] once the bot is constructed. Override to plug in a custom manager.
 */
@ConfigurationDSL
data class SessionConfiguration(
    var keyStrategy: SessionKeyStrategy = SessionKeyStrategy.ChatUser,
    var storage: SessionStorage = InMemorySessionStorage(),
    var managerFactory: SessionManagerFactory = SessionManagerFactory { bot, cfg ->
        DefaultSessionManager(bot, cfg.storage, cfg.keyStrategy, bot.config.loggerFactory)
    },
) {
    /**
     * Manager produced by [managerFactory]; populated once by `TelegramBot.init { }`.
     */
    internal lateinit var manager: SessionManager
}
