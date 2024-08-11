package eu.vendeli.ktgram.config.env

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlInputConfig
import eu.vendeli.tgbot.interfaces.helper.ConfigLoader
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration
import eu.vendeli.tgbot.types.internal.configuration.CompleteConfig
import kotlinx.serialization.serializer

open class TomlConfigLoader(input: String) : ConfigLoader {
    protected val instance = Toml(
        inputConfig = TomlInputConfig(
            // allow/prohibit unknown names during the deserialization, default false
            ignoreUnknownNames = false,
            // allow/prohibit empty values like "a = # comment", default true
            allowEmptyValues = true,
            // allow/prohibit null values like "a = null", default true
            allowNullValues = true,
            // allow/prohibit escaping of single quotes in literal strings, default true
            allowEscapedQuotesInLiteralStrings = true,
            // allow/prohibit processing of empty toml, if false - throws an InternalDecodingException exception, default is true
            allowEmptyToml = true,
        ),
    )
    private val toml = instance.decodeFromString(serializer<CompleteConfig>(), input)
    override val token: String = toml.token
    override val commandsPackage: String? = toml.pckg

    override fun load(): BotConfiguration = toml.configuration
}
