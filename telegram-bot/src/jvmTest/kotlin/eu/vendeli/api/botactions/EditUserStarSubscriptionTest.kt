package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.editUserStarSubscription

class EditUserStarSubscriptionTest : BotTestContext() {
    @Test
    suspend fun `edit user star subscription test`() {
        editUserStarSubscription(TG_ID, "test", true)
            .sendAsync(bot)
            .shouldFailure() shouldContainInDescription "CHARGE_ID_INVALID"
    }
}
