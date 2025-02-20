package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.common.dice
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class DiceTest : BotTestContext() {
    @Test
    suspend fun `dice method test`() {
        val result = dice(emoji = "\uD83C\uDFC0")
            .options {
                protectContent = true
            }.sendReq()
            .shouldSuccess()

        result.hasProtectedContent shouldBe true

        with(result.dice) {
            shouldNotBeNull()
            emoji shouldBe "\uD83C\uDFC0"
            value.shouldNotBeNull()
        }
    }
}
