package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.core.InputListenerMapImpl
import eu.vendeli.tgbot.types.User
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

        val user = User(1, false, "Test")
        mapImpl.set(user) { "test2" }
        mapImpl[user] shouldBe "test2"

        mapImpl[user] = "test3"
        mapImpl[user] shouldBe "test3"
    }

    @Test
    suspend fun `async crud test`() {
        mapImpl.setAsync(1, "test").await().shouldBeTrue()
        mapImpl.getAsync(1).await() shouldBe "test"
        mapImpl.delAsync(1).await().shouldBeTrue()
        mapImpl.getAsync(1).shouldBeNull()

        val user = User(1, false, "Test")
        mapImpl.setAsync(user) { "test2" }.await().shouldBeTrue()
        mapImpl.getAsync(user.id) shouldBe "test2"
    }
}
