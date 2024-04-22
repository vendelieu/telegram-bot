package eu.vendeli.spring.starter

import eu.vendeli.tgbot.types.internal.configuration.CommandParsingConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "ktgram")
data class TgConfigProperties @ConstructorBinding constructor(
    val autoStartPolling: Boolean = true,
    val bot: List<BotProperties>
) {
    data class BotProperties(
        val token: String,
        val pckg: String? = null,
        val identifier: String = "KtGram",
        val commandParsing: CommandParsingConfiguration? = null,
    )
}
