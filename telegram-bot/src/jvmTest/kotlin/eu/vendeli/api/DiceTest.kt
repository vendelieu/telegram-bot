package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.dice
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class DiceTest : BotTestContext() {
    @Test
    suspend fun `dice method test`() {
        val result = dice(emoji = "\uD83C\uDFC0").options {
            protectContent = true
        }.sendReturning(TG_ID, bot).shouldSuccess()

        result.hasProtectedContent shouldBe true

        with(result.dice) {
            shouldNotBeNull()
            emoji shouldBe "\uD83C\uDFC0"
            value.shouldNotBeNull()
        }
    }
}
