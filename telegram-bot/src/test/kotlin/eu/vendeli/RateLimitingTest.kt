package eu.vendeli

import BotTestContext
import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.shouldBe
import java.util.concurrent.atomic.AtomicInteger

class RateLimitingTest : BotTestContext(false, true) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest
    private fun prepareBot() {
        bot = TelegramBot("not necessary") {
            logging {
                botLogLevel = Level.INFO
            }
            rateLimiter {
                limits = RateLimits(10000, 5)
            }
        }
    }

    @Test
    suspend fun `test limit exceeding`() {
        prepareBot()
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
        prepareBot()
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
