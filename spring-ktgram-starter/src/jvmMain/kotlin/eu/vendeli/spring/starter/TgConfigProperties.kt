package eu.vendeli.spring.starter

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

/**
 * Configuration properties for KtGram.
 *
 * @property autoStartPolling Start long polling automatically after the bot is created.
 * @property shareHttpClient Share the same HTTP client instance between all bots.
 * @property maxHandlingRetries The maximum number of retries for handling updates.
 * @property bot The list of bot configurations.
 */
@ConfigurationProperties(prefix = "ktgram")
data class TgConfigProperties
    @ConstructorBinding
    constructor(
        val autoStartPolling: Boolean = true,
        val shareHttpClient: Boolean = false,
        val maxHandlingRetries: Int = 5,
        val bot: List<BotProperties>,
    ) {
        /**
         * Configuration properties for a bot.
         *
         * @property token The bot token.
         * @property pckg The package name for the bot.
         * @property identifier The identifier for this bot configuration.
         */
        data class BotProperties(
            /**
             * The bot token.
             */
            val token: String,
            /**
             * The package name for the bot.
             */
            val pckg: String? = null,
            /**
             * The identifier for this bot configuration.
             */
            val identifier: String = "KtGram",
        )
    }
