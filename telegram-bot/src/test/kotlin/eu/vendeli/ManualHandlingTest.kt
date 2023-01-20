package eu.vendeli

import BotTestContext
import io.kotest.matchers.shouldBe
import java.util.concurrent.atomic.AtomicInteger

class ManualHandlingTest : BotTestContext(true, true) {
    @Test
    suspend fun `input chaining`() {
        val loopCounter = AtomicInteger(0)

        bot.handleUpdates {
            inputChain("test") {
                user shouldBe 1
            }.breakIf({ update.message != null }, false) {
                update.message?.chat?.id shouldBe 1
            }.andThen {
                user shouldBe 2
                // processing should not reach this point so the check will not interrupt the test
            }
            if (loopCounter.incrementAndGet() == 5) bot.update.stopListener()
        }
    }

    @Test
    suspend fun `input chaining repeating`() {
        val loopCounter = AtomicInteger(0)

        bot.handleUpdates {
            inputChain("test") {
                user shouldBe 1
            }.breakIf({ loopCounter.incrementAndGet() < 3 }) {
                update.message?.chat?.id shouldBe 1
            }.andThen {
                // processing should come there for 3 loop try
                loopCounter.get() shouldBe 3
                update.message?.text shouldBe "/start"
            }
            if (loopCounter.incrementAndGet() == 5) bot.update.stopListener()
        }
    }
}
