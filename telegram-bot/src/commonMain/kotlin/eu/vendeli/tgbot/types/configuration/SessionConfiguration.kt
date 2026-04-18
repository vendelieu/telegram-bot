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
 * Calling `sessions { }` (even with an empty body) in [BotConfiguration] opts into the feature;
 * leaving it out keeps `TelegramBot.sessions` at `null` and skips the tracking interceptor.
 *
 * @property keyStrategy How session keys are derived from a [eu.vendeli.tgbot.types.component.ProcessedUpdate].
 * @property trackIncoming When `true`, a pipeline interceptor records every incoming message-bearing update into its session.
 * @property storage Backend that stores tracked messages. Consumed by the default factory; custom factories may ignore it.
 * @property managerFactory Builds the [SessionManager] once the bot is constructed. Override to plug in a custom manager.
 */
@ConfigurationDSL
data class SessionConfiguration(
    var keyStrategy: SessionKeyStrategy = SessionKeyStrategy.ChatUser,
    var trackIncoming: Boolean = true,
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
