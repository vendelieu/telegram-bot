package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.core.InputListenerMapImpl
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class InputListenerTest : BotTestContext() {
    private val mapImpl = InputListenerMapImpl()

    @Test
    fun `sync crud test`() {
        mapImpl.set(1, "test")
        mapImpl.get(1) shouldBe "test"
        mapImpl.del(1)
    }

    @Test
    suspend fun `async crud test`() {
        mapImpl.setAsync(1, "test").await().shouldBeTrue()
        mapImpl.getAsync(1).await() shouldBe "test"
        mapImpl.delAsync(1).await().shouldBeTrue()
    }
}
