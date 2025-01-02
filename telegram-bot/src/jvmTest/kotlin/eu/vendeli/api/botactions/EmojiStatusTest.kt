package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.setUserEmojiStatus

class EmojiStatusTest : BotTestContext() {
    @Test
    suspend fun `set emoji status test`() {
        setUserEmojiStatus(TG_ID)
            .sendReturning(bot)
            .shouldFailure() shouldContainInDescription "not enough rights to change the user's emoji status"
    }
}
