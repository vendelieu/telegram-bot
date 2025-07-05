package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.payments.getMyStarBalance
import io.kotest.matchers.shouldBe

class StarTest : BotTestContext() {
    @Test
    suspend fun `getMyStarBalance method test`() {
        val result = getMyStarBalance().sendReq().shouldSuccess()

        result.amount shouldBe 0
    }
}
