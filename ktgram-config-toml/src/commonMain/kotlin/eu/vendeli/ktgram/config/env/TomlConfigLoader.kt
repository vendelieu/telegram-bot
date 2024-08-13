package eu.vendeli.ktgram.config.env

import eu.vendeli.tgbot.interfaces.helper.ConfigLoader
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration
import eu.vendeli.tgbot.types.internal.configuration.CompleteConfig
import kotlinx.serialization.serializer
import net.peanuuutz.tomlkt.Toml

open class TomlConfigLoader(input: String) : ConfigLoader {
    private val toml = Toml.decodeFromString(serializer<CompleteConfig>(), input)
    override val token: String = toml.token
    override val commandsPackage: String? = toml.pckg

    override fun load(): BotConfiguration = toml.configuration
}
