package eu.vendeli.spring.starter

import eu.vendeli.tgbot.TelegramBot
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@AutoConfiguration
@Import(SpringClassManager::class)
@EnableConfigurationProperties(TgConfigProperties::class)
open class TelegramAutoConfiguration(
    private val config: TgConfigProperties,
    private val springClassManager: SpringClassManager,
) {
    @Autowired(required = false)
    private lateinit var cfg: BotConfiguration

    @Bean
    open fun botInstances(): List<TelegramBot> = config.bot.map {
        TelegramBot(it.token, it.pckg) {
            classManager = springClassManager
            if (this@TelegramAutoConfiguration::cfg.isInitialized && cfg.identifier == it.identifier) {
                this.apply(cfg.applyCfg())
            }
        }.also { bot -> bot.identifier = it.identifier }.tryRunHandler()
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
