import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.HttpLogLevel
import io.kotest.core.spec.style.AnnotationSpec

abstract class BotTestContext(private val withPreparedBot: Boolean = true) : AnnotationSpec() {
    protected lateinit var bot: TelegramBot
    protected var classloader: ClassLoader = Thread.currentThread().contextClassLoader

    @BeforeAll
    fun prepareTestBot() {
        if (withPreparedBot) bot = TelegramBot(System.getenv("BOT_TOKEN"), "eu.vendeli") {
            logging {
                botLogLevel = Level.TRACE
                httpLogLevel = HttpLogLevel.ALL
            }
            httpClient {
                maxRequestRetry = 0
            }
        }
    }
}
