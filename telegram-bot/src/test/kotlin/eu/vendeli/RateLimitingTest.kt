package eu.vendeli

import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.core.TokenBucketLimiterImpl
import eu.vendeli.tgbot.types.*
import eu.vendeli.tgbot.types.internal.RateLimits
import eu.vendeli.tgbot.types.internal.Response
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RateLimitingTest {
    private lateinit var bot: TelegramBot

    @BeforeAll
    fun prepareTestBot() {
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
    fun `test limit exceeding`() = runBlocking {
        val hitsCounter = AtomicInteger(0)
        val loopsCounter = AtomicInteger(0)

        bot.update.setListener {
            bot.update.handle(it) {
                onMessage {
                    hitsCounter.incrementAndGet()
                }
            }
            if (loopsCounter.incrementAndGet() == 10) stopListener()
        }
        assertEquals(hitsCounter.get(), 5)
        assertEquals(loopsCounter.get(), 10)
    }

    @Test
    fun `test certain command limit exceeding`() = runBlocking {
        val messageHitsCounter = AtomicInteger(0)
        val commandHitsCounter = AtomicInteger(0)
        val loopsCounter = AtomicInteger(0)

        bot.update.setListener {
            bot.update.handle(it) {
                onMessage {
                    messageHitsCounter.incrementAndGet()
                }
                onCommand("/start", RateLimits(10000, 2)) {
                    commandHitsCounter.incrementAndGet()
                }
            }
            if (loopsCounter.incrementAndGet() == 20) stopListener()
        }
        assertEquals(messageHitsCounter.get(), 5)
        assertEquals(commandHitsCounter.get(), 2)
        assertEquals(loopsCounter.get(), 20)
    }
}