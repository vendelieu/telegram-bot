
import eu.vendeli.fixtures.__ACTIVITIES
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.MediaAction
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.component.HttpLogLevel
import eu.vendeli.tgbot.types.component.LogLvl
import eu.vendeli.tgbot.types.component.Response
import eu.vendeli.tgbot.types.component.getOrNull
import eu.vendeli.tgbot.types.component.isSuccess
import eu.vendeli.tgbot.utils.common.GET_UPDATES_ACTION
import eu.vendeli.tgbot.utils.common.defineActivities
import eu.vendeli.tgbot.utils.common.serde
import eu.vendeli.utils.MockUpdate
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.common.ExperimentalKotest
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import io.ktor.http.isSuccess
import io.ktor.util.logging.KtorSimpleLogger
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import utils.BotResource
import utils.RandomPicResource
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@Suppress("VariableNaming", "PropertyName", "PrivatePropertyName", "SpellCheckingInspection")
abstract class BotTestContext(
    private val withPreparedBot: Boolean = true,
    private val mockHttp: Boolean = false,
    private val spykIt: Boolean = true,
) : AnnotationSpec() {
    override fun threads() = 1

    @ExperimentalKotest
    override fun concurrency() = 1

    private val INT_ITERATOR = (1..Int.MAX_VALUE).iterator()
    private val RANDOM_INST: Random get() = Random(CUR_INSTANT.epochSeconds)
    internal lateinit var bot: TelegramBot
    internal val updatesAction = spyk(GET_UPDATES_ACTION)
    protected var classloader: ClassLoader = Thread.currentThread().contextClassLoader

    protected val TG_ID by lazy { System.getenv("TELEGRAM_ID").toLong() }
    protected var BOT_ID by Delegates.notNull<Long>()
    protected val CHAT_ID by lazy { System.getenv("CHAT_ID").toLong() }
    protected val CHANNEL_ID by lazy { System.getenv("CHANNEL_ID").toLong() }
    protected val PAYMENT_PROVIDER_TOKEN: String? by lazy { System.getenv("PAYMENT_PROVIDER_TOKEN") }

    protected val RANDOM_PIC: ByteArray?
        get() = getRandomPic() ?: run {
            RandomPicResource.swapAndGet()
            getRandomPic()
        }

    protected val CUR_INSTANT: Instant get() = Clock.System.now()
    protected val ITER_INT: Int get() = INT_ITERATOR.nextInt()
    protected val RAND_INT: Int get() = RANDOM_INST.nextInt()
    protected val DUMB_USER = User(id = 1, isBot = false, firstName = "Test")
    protected val DUMB_CHAT = Chat(id = -1, type = ChatType.Group, title = "test")
    protected val logger = KtorSimpleLogger("test.eu.vendeli.ktgram")

    @BeforeAll
    fun prepareTestBot() {
        val ctx = BotResource.swapAndGet()
        BOT_ID = ctx.id
        if (withPreparedBot) bot = TelegramBot(ctx.token, "eu.vendeli") {
            httpClient {
                maxRequestRetry = 0
                connectTimeoutMillis = 10.seconds.inWholeMilliseconds
                httpLogLevel = HttpLogLevel.BODY
            }
            updatesListener {
                pullingDelay = 100
            }
        }
        bot.defineActivities(__ACTIVITIES)
        if (spykIt) spykIt()

        if (mockHttp) doMockHttp()
    }

    fun doMockHttp(mockUpdates: MockUpdate = MockUpdate.SINGLE()) {
        mockkStatic(::GET_UPDATES_ACTION)
        every { ::GET_UPDATES_ACTION.invoke() } returns updatesAction
        coEvery { updatesAction.sendReturning(any()).await() } returns Response.Success(mockUpdates.updates)
    }

    fun spykIt() {
        bot = spyk(bot, recordPrivateCalls = true)
    }

    private fun getRandomPic(): ByteArray? = runBlocking {
        bot.httpClient.get(RandomPicResource.RANDOM_PIC_URL).takeIf { it.status.isSuccess() }?.readRawBytes()?.also {
            logger.warn("RANDOM PIC OBTAINING ERROR.")
        }
    }

    protected suspend fun <T> Action<T>.sendReq(to: Long = TG_ID, via: TelegramBot = bot): Response<out T> {
        delay(RANDOM_INST.nextLong(10, 200))
        return sendReturning(to, via).await()
    }

    protected suspend fun <T : Any> SimpleAction<T>.sendReq(via: TelegramBot = bot): Response<out T> {
        delay(RANDOM_INST.nextLong(10, 200))
        return sendReturning(via).await()
    }

    protected suspend fun <T> MediaAction<T>.sendReq(to: Long = TG_ID, via: TelegramBot = bot): Response<out T> {
        delay(RANDOM_INST.nextLong(10, 200))
        return sendReturning(to, via).await()
    }

    protected fun Long.asUser() = User(this, false, "test")
    protected fun Long.asChat() = Chat(this, ChatType.Private)

    protected suspend inline fun <T> Deferred<Response<out T>>.shouldSuccess() = await().shouldSuccess()

    protected inline fun <T> Response<T>.shouldSuccess() = with(this) {
        ok.shouldBeTrue()
        isSuccess().shouldBeTrue()
        getOrNull().shouldNotBeNull()
    }

    protected suspend inline fun <T> Deferred<Response<out T>>.shouldFailure() = await().shouldFailure()

    protected inline fun <T> Response<T>.shouldFailure(): Response.Failure = with(this) {
        ok.shouldBeFalse()
        isSuccess().shouldBeFalse()
        getOrNull().shouldBeNull()
        this as Response.Failure
    }

    protected inline infix fun Response.Failure.shouldContainInDescription(text: String) =
        description.shouldContain(text)

    internal inline fun <reified T> JsonElement.isSerializableTo(): T = shouldNotThrowAny {
        serde.decodeFromJsonElement(
            serializer(T::class.java),
            this,
        ) as T
    }
}
