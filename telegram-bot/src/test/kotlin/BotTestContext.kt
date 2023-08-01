import ch.qos.logback.classic.Level.TRACE
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.HttpLogLevel
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.utils.MockUpdate
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.AnnotationSpec
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import java.time.Instant

@Suppress("VariableNaming", "PropertyName", "PrivatePropertyName")
abstract class BotTestContext(
    private val withPreparedBot: Boolean = true,
    private val mockHttp: Boolean = false,
) : AnnotationSpec() {
    protected lateinit var bot: TelegramBot
    protected var classloader: ClassLoader = Thread.currentThread().contextClassLoader

    private val TOKEN by lazy { System.getenv("BOT_TOKEN") }
    protected val TG_ID by lazy { System.getenv("TELEGRAM_ID").toLong() }
    protected val BOT_ID by lazy { TOKEN.substringBefore(':').toLong() }

    protected val RANDOM_PIC_URL = "https://picsum.photos/10"
    protected val RANDOM_PIC by lazy { runBlocking { bot.httpClient.get(RANDOM_PIC_URL).readBytes() } }
    protected val CUR_TIMESTAMP: Instant get() = Instant.now()
    protected val ITER_INT: Int get() = (1..Int.MAX_VALUE).iterator().nextInt()

    @BeforeAll
    fun prepareTestBot() {
        if (withPreparedBot) bot = TelegramBot(TOKEN, "eu.vendeli") {
            logging {
                botLogLevel = TRACE
                httpLogLevel = HttpLogLevel.ALL
            }
            httpClient {
                maxRequestRetry = 0
            }
        }

        if (mockHttp) doMockHttp()
    }

    fun doMockHttp(mockUpdates: MockUpdate = MockUpdate.SINGLE()) {
        bot.httpClient = HttpClient(
            MockEngine {
                respond(
                    content = ByteReadChannel(mockUpdates.response),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )
    }

    protected suspend fun <T> Action<T>.sendReturning(id: Long, bot: TelegramBot): Response<out T> =
        sendAsync(id, bot).await()

    protected suspend fun getExtFile(url: String): ByteArray = bot.httpClient.get(url).readBytes()
}
