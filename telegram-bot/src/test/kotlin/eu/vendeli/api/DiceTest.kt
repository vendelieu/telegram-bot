package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.dice
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class DiceTest : BotTestContext() {
    @Test
    suspend fun `location method test`() {
        val request = dice(emoji = "\uD83C\uDFC0").sendAsync(TG_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        with(result.dice) {
            shouldNotBeNull()
            emoji shouldBe "\uD83C\uDFC0"
            value.shouldNotBeNull()
        }
    }
}
