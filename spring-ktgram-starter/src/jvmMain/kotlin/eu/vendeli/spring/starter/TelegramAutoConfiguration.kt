package eu.vendeli.spring.starter

import eu.vendeli.tgbot.TelegramBot
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@AutoConfiguration
@Import(SpringClassManager::class)
@EnableConfigurationProperties(TgConfigProperties::class)
open class TelegramAutoConfiguration(
    private val config: TgConfigProperties,
    private val springClassManager: SpringClassManager
) {

    @Bean
    open fun botInstances(): List<TelegramBot> = config.bot.map {
        TelegramBot(it.token, it.pckg) {
            classManager = springClassManager
            it.commandParsing?.let { c ->
                commandParsing {
                    commandDelimiter = c.commandDelimiter
                    parametersDelimiter = c.parametersDelimiter
                    parameterValueDelimiter = c.parameterValueDelimiter
                    restrictSpacesInCommands = c.restrictSpacesInCommands
                }
            }
        }.tryRunHandler()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun TelegramBot.tryRunHandler(): TelegramBot {
        if (config.autoStartPolling) GlobalScope.launch(Dispatchers.IO) {
            launch(Dispatchers.IO) {
                handleUpdates()
            }
        }
        return this
    }
}
