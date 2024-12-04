package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.savePreparedInlineMessage
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.utils.LOREM
import io.kotest.matchers.booleans.shouldBeTrue
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant

class SavePreparedInlineMessageTest : BotTestContext() {
    @Test
    suspend fun `save prepared inline message test`() {
        val result = savePreparedInlineMessage(TG_ID) {
            InlineQueryResult.Audio("test", LOREM.AUDIO.url, "testTitle")
        }.options {
            allowBotChats = true
        }.sendAsync(bot)
            .shouldSuccess()

        Clock.System
            .now()
            .toJavaInstant()
            .isBefore(result.expirationDate.toJavaInstant())
            .shouldBeTrue()
    }

    @Test
    suspend fun `save prepared inline message without allowing options test`() {
        val result = savePreparedInlineMessage(TG_ID) {
            InlineQueryResult.Audio("test", LOREM.AUDIO.url, "testTitle")
        }.sendAsync(bot)
            .shouldFailure() shouldContainInDescription "Bad Request: at least one chat type must be allowed"
    }
}
