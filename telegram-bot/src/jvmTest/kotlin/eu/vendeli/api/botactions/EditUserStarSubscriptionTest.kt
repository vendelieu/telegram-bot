@file:Suppress("ktlint:standard:function-naming")

package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.payments.editUserStarSubscription

class EditUserStarSubscriptionTest : BotTestContext() {
    @Test
    suspend fun `edit user star subscription test`() {
        editUserStarSubscription(TG_ID, "test", true)
            .sendReturning(bot)
            .shouldFailure() shouldContainInDescription "CHARGE_ID_INVALID"
    }
}
