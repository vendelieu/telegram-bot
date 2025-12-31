package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.implementations.TokenBucketLimiterImpl
import eu.vendeli.tgbot.types.component.LogLvl
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.onMessage
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.shouldBe
import java.util.concurrent.atomic.AtomicInteger

class RateLimitingTest : BotTestContext(mockHttp = true) {
    private val limiter = TokenBucketLimiterImpl()
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    @BeforeEach
    fun prepareBot() = bot.config.run {
        limiter.resetState()
        spykIt()
        doMockHttp()
        rateLimiter {
            mechanism = limiter
            limits = RateLimits(10000, 5)
        }
    }

    @Test
    suspend fun `test limit exceeding`() {
        var exceeded = false
        val hitsCounter = AtomicInteger(0)
        val loopCounter = AtomicInteger(0)
        bot.config.rateLimiter.exceededAction = { _: Long, _: TelegramBot ->
            exceeded = true
        }

        bot.setFunctionality {
            onMessage {
                hitsCounter.incrementAndGet()
            }
        }
        bot.update.setListener {
            if (loopCounter.incrementAndGet() == 10) stopListener()
            bot.update.handle(it)
        }
        hitsCounter.get() shouldBe 5
        loopCounter.get() shouldBe 10
        exceeded shouldBe true
    }

    @Test
    suspend fun `test certain command limit exceeding`() {
        var exceeded = false
        val messageHitsCounter = AtomicInteger(0)
        val commandHitsCounter = AtomicInteger(0)
        val loopsCounter = AtomicInteger(0)
        bot.config.rateLimiter.exceededAction = { _: Long, _: TelegramBot ->
            exceeded = true
        }

        bot.setFunctionality {
            onMessage {
                messageHitsCounter.incrementAndGet()
            }
            onCommand("/start", rateLimits = RateLimits(10_000, 2)) {
                commandHitsCounter.incrementAndGet()
            }
        }
        bot.update.setListener {
            if (loopsCounter.incrementAndGet() == 20) stopListener()
            bot.update.handle(it)
        }
        messageHitsCounter.get() shouldBe 2
        commandHitsCounter.get() shouldBe 2
        loopsCounter.get() shouldBe 20
        exceeded shouldBe true
    }
}
