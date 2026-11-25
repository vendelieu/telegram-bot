package eu.vendeli.quarkus.extension;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.List;
import java.util.Optional;

/**
 * Configuration properties for KtGram.
 * <p>
 * Written in Java on purpose: Quarkus' extension annotation processor only runs on Java sources,
 * and it is what registers this class as a config root.
 */
@ConfigMapping(prefix = "ktgram")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface KtGramConfig {

    /**
     * Start long polling automatically after the bot is created.
     */
    @WithDefault("true")
    boolean autoStartPolling();

    /**
     * Share the application's {@code io.ktor.client.HttpClient} bean between all bots.
     */
    @WithDefault("false")
    boolean shareHttpClient();

    /**
     * The maximum number of retries for handling updates.
     */
    @WithDefault("5")
    int maxHandlingRetries();

    /**
     * The list of bot configurations.
     */
    List<Bot> bot();

    /**
     * Configuration of a single bot.
     */
    interface Bot {

        /**
         * The bot token.
         */
        String token();

        /**
         * The package to scan for the bot's handlers.
         */
        Optional<String> pckg();

        /**
         * The identifier for this bot, used to match a {@code BotConfiguration} bean.
         */
        @WithDefault("KtGram")
        String identifier();
    }
}
