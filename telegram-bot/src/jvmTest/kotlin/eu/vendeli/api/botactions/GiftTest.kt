package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.getAvailableGifts
import eu.vendeli.tgbot.api.botactions.sendGift
import io.kotest.matchers.collections.shouldHaveAtLeastSize

class GiftTest : BotTestContext() {
    @Test
    suspend fun `get available gifts test`() {
        val result = getAvailableGifts().sendReturning(bot).shouldSuccess()

        result.gifts shouldHaveAtLeastSize 1
    }

    @Test
    suspend fun `send gift test`() {
        val available = getAvailableGifts().sendReturning(bot).shouldSuccess()
        val result = sendGift(TG_ID, available.gifts.first().id) {
            "test"
        }.sendReturning(bot).shouldFailure() shouldContainInDescription "BALANCE_TOO_LOW"
    }
}
