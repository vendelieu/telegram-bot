package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.implementations.InputListenerMapImpl
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class InputListenerTest : BotTestContext() {
    private val mapImpl = InputListenerMapImpl()

    @Test
    fun `sync crud test`() {
        mapImpl.set(1, "test")
        mapImpl.get(1) shouldBe "test"
        mapImpl.del(1)
        mapImpl.get(1).shouldBeNull()

        mapImpl.set(DUMB_USER) { "test2" }
        mapImpl[DUMB_USER] shouldBe "test2"

        mapImpl[DUMB_USER] = "test3"
        mapImpl[DUMB_USER] shouldBe "test3"
    }

    @Test
    suspend fun `async crud test`() {
        mapImpl.setAsync(1, "test").await().shouldBeTrue()
        mapImpl.getAsync(1).await() shouldBe "test"
        mapImpl.delAsync(1).await().shouldBeTrue()
        mapImpl.getAsync(1).await().shouldBeNull()

        mapImpl.setAsync(DUMB_USER) { "test2" }.await().shouldBeTrue()
        mapImpl.getAsync(DUMB_USER.id).await() shouldBe "test2"
    }
}
