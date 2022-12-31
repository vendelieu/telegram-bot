package eu.vendeli

import BotTestContext
import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.core.TokenBucketLimiterImpl
import eu.vendeli.tgbot.types.*
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong

class RateLimitingTest : BotTestContext(false) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    @BeforeAll
    fun prepareBot() {
        bot = TelegramBot("not necessary") {
            rateLimiter = TokenBucketLimiterImpl()
            logging {
                botLogLevel = Level.INFO
            }
            rateLimits {
                period = 10000
                rate = 5
            }
        }
        val testUser = User(1, false, "Test")
        val testMsg =
            Message(nextLong(), from = testUser, chat = Chat(1, ChatType.Private), date = nextInt(), text = "/start")
        val apiResponse = Response.Success(
            listOf(
                Update(nextInt(), testMsg),
                Update(nextInt(), testMsg),
            )
        )
        bot.httpClient = HttpClient(MockEngine {
            respond(
                content = ByteReadChannel(mapper.writeValueAsBytes(apiResponse)),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
    }

    @Test
    suspend fun `test limit exceeding`() {
        val hitsCounter = AtomicInteger(0)
        val loopCounter = AtomicInteger(0)

        bot.update.setListener {
            if (loopCounter.incrementAndGet() == 10) stopListener()
            bot.update.handle(it) {
                onMessage {
                    hitsCounter.incrementAndGet()
                }
            }
        }
        hitsCounter.get() shouldBe 5
        loopCounter.get() shouldBe 10
    }

    @Test
    suspend fun `test certain command limit exceeding`() {
        val messageHitsCounter = AtomicInteger(0)
        val commandHitsCounter = AtomicInteger(0)
        val loopsCounter = AtomicInteger(0)

        bot.update.setListener {
            if (loopsCounter.incrementAndGet() == 20) stopListener()
            bot.update.handle(it) {
                onMessage {
                    messageHitsCounter.incrementAndGet()
                }
                onCommand("/start", RateLimits(10000, 2)) {
                    commandHitsCounter.incrementAndGet()
                }
            }
        }
        messageHitsCounter.get() shouldBe 5
        commandHitsCounter.get() shouldBe 2
        loopsCounter.get() shouldBe 20
    }
}