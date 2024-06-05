package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.refundStarPayment
import eu.vendeli.tgbot.types.internal.onFailure
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.string.shouldContain

class RefundStarPaymentTest : BotTestContext() {
    @Test
    suspend fun `refundStarPayment method testing`() {
        refundStarPayment(
            "test",
            DUMB_USER.id,
        ).sendAsync(bot).onFailure {
            it.description shouldContain "USER_ID_INVALID"
        }?.shouldBeFalse()

    }
}
