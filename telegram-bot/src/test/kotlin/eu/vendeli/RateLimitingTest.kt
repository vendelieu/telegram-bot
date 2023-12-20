package eu.vendeli

import BotTestContext
import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.implementations.TokenBucketLimiterImpl
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.shouldBe
import java.util.concurrent.atomic.AtomicInteger

class RateLimitingTest : BotTestContext(mockHttp = true) {
    private val limiter = TokenBucketLimiterImpl()
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    @OptIn(ExperimentalFeature::class)
    @BeforeEach
    fun prepareBot() = bot.config.run {
        limiter.resetState()
        rateLimiter {
            mechanism = limiter
            limits = RateLimits(10000, 5)
        }
        logging {
            botLogLevel = Level.DEBUG
        }
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
                onCommand("/start", rateLimits = RateLimits(10000, 2)) {
                    commandHitsCounter.incrementAndGet()
                }
            }
        }
        messageHitsCounter.get() shouldBe 5
        commandHitsCounter.get() shouldBe 2
        loopsCounter.get() shouldBe 20
    }
}
