package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.payments.refundStarPayment
import eu.vendeli.tgbot.types.component.onFailure
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.string.shouldContain

class RefundStarPaymentTest : BotTestContext() {
    @Test
    suspend fun `refundStarPayment method testing`() {
        refundStarPayment(
            "test",
            DUMB_USER.id,
        ).sendReq()
            .onFailure {
                it.description shouldContain "CHARGE_ID_EMPTY"
            }?.shouldBeFalse()
    }
}
