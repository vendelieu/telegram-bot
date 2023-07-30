package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.game
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class GameTest : BotTestContext() {
    @Test
    suspend fun `send game method test`() {
        val request = game("testestes").sendAsync(TG_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result.game) {
            shouldNotBeNull()
            title shouldBe "test"
            description shouldBe "test2"
        }
    }
}
