
import eu.vendeli.fixtures.`$ACTIONS_eu_vendeli_fixtures`
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.CodegenUpdateHandler
import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.HttpLogLevel
import eu.vendeli.tgbot.types.internal.LogLvl
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import eu.vendeli.utils.MockUpdate
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import java.time.Instant
import kotlin.properties.Delegates
import kotlin.random.Random

private val mutex = Mutex()
private suspend fun Mutex.toggle() = if (isLocked) unlock() else lock()
private val BOT_DATA: Iterator<Pair<Long, String>> = generateSequence {
    runBlocking {
        mutex.toggle()
        System.getenv("BOT_TOKEN" + if (mutex.isLocked) "_2" else "").let {
            it.substringBefore(':').toLong() to it
        }
    }
}.iterator()

@Suppress("VariableNaming", "PropertyName", "PrivatePropertyName", "SpellCheckingInspection")
abstract class BotTestContext(
    private val withPreparedBot: Boolean = true,
    private val mockHttp: Boolean = false,
    private val spykIt: Boolean = true,
) : AnnotationSpec() {
    private val INT_ITERATOR = (1..Int.MAX_VALUE).iterator()
    private val RANDOM_INST: Random get() = Random(CUR_INSTANT.epochSecond)
    internal lateinit var bot: TelegramBot
    protected var classloader: ClassLoader = Thread.currentThread().contextClassLoader

    private val BOT_CTX get() = BOT_DATA.next()
    protected val TG_ID by lazy { System.getenv("TELEGRAM_ID").toLong() }
    protected var BOT_ID by Delegates.notNull<Long>()
    protected val CHAT_ID by lazy { System.getenv("CHAT_ID").toLong() }
    protected val PAYMENT_PROVIDER_TOKEN = "1877036958:TEST:5a97ee6bbb1010e9c1033d00979832763c7622a4"

    protected val RANDOM_PIC_URL = "https://picsum.photos/10"
    protected val RANDOM_PIC by lazy { runBlocking { bot.httpClient.get(RANDOM_PIC_URL).readBytes() } }
    protected val CUR_INSTANT: Instant get() = Instant.now()
    protected val ITER_INT: Int get() = INT_ITERATOR.nextInt()
    protected val RAND_INT: Int get() = RANDOM_INST.nextInt()
    protected val DUMB_USER = User(1, false, "Test")

    @BeforeAll
    fun prepareTestBot() {
        val ctx = BOT_CTX
        BOT_ID = ctx.first
        if (withPreparedBot) bot = TelegramBot(ctx.second, "eu.vendeli") {
            logging {
                botLogLevel = LogLvl.TRACE
                httpLogLevel = HttpLogLevel.ALL
            }
            httpClient {
                maxRequestRetry = 0
            }
            updatesListener {
                pullingDelay = 100
            }
        }
        if (spykIt) spykIt()

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

    fun spykIt() {
        bot = spyk(bot, recordPrivateCalls = true)
        every { bot.update } returns CodegenUpdateHandler(`$ACTIONS_eu_vendeli_fixtures`, bot)
    }

    protected suspend fun <T> Action<T>.sendReturning(id: Long, bot: TelegramBot): Response<out T> {
        delay(200)
        return sendAsync(id, bot).await()
    }

    protected fun Long.asUser() = User(this, false, "test")
    protected fun Long.asChat() = Chat(this, ChatType.Private)

    protected suspend fun getExtFile(url: String): ByteArray = bot.httpClient.get(url).readBytes()

    protected suspend inline fun <T> Deferred<Response<out T>>.shouldSuccess() = await().shouldSuccess()
    @Suppress("NOTHING_TO_INLINE")
    protected inline fun <T> Response<T>.shouldSuccess() = with(this) {
        ok.shouldBeTrue()
        isSuccess().shouldBeTrue()
        getOrNull().shouldNotBeNull()
    }
}
