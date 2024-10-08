package eu.vendeli.spring.starter

import eu.vendeli.tgbot.TelegramBot
import io.ktor.client.HttpClient
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
    private var cfg: List<BotConfiguration>? = null

    @Autowired(required = false)
    private var client: HttpClient? = null

    @Bean
    @OptIn(DelicateCoroutinesApi::class)
    open fun botInstances(): List<TelegramBot> = config.bot.map { bot ->
        val botCfg = cfg?.find { bot.identifier == it.identifier }
        val botInstance = TelegramBot(bot.token, bot.pckg, client.takeIf { config.shareHttpClient }) {
            classManager = springClassManager
            identifier = bot.identifier
            botCfg?.let { apply(it.applyCfg()) }
        }

        if (botCfg?.autostartLongPolling != false && config.autoStartPolling) {
            GlobalScope.launch(Dispatchers.IO) {
                botCfg?.onInit(botInstance)
                launch(Dispatchers.IO) {
                    botInstance.handleUpdates(botCfg?.allowedUpdates)
                }
            }
        }

        return@map botInstance
    }
}
