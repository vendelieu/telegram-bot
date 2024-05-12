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
    private lateinit var cfg: List<BotConfiguration>

    @Bean
    @OptIn(DelicateCoroutinesApi::class)
    open fun botInstances(): List<TelegramBot> = config.bot.map { bot ->
        val botCfg = if (this::cfg.isInitialized) cfg.find { bot.identifier == it.identifier } else null
        val botInstance = TelegramBot(bot.token, bot.pckg) {
            classManager = springClassManager
            botCfg?.let { this.apply(it.applyCfg()) }
        }
        botInstance.identifier = bot.identifier

        if (botCfg?.autostartLongPolling != false && config.autoStartPolling) {
            GlobalScope.launch(Dispatchers.IO) {
                launch(Dispatchers.IO) {
                    botInstance.handleUpdates(botCfg?.allowedUpdates)
                }
            }
        }

        return@map botInstance
    }
}
