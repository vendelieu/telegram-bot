package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.savePreparedInlineMessage
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.utils.LOREM
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.string.shouldNotBeBlank

class SavePreparedInlineMessageTest : BotTestContext() {
    @Test
    suspend fun `save prepared inline message test`() {
        val result = savePreparedInlineMessage(TG_ID) {
            InlineQueryResult.Audio("test", LOREM.AUDIO.url, "testTitle")
        }.options {
            allowBotChats = true
        }.sendReturning(bot)
            .shouldSuccess()

        result.id.shouldNotBeBlank()
        result.expirationDate.epochSeconds shouldBeGreaterThan 0L
    }

    @Test
    suspend fun `save prepared inline message without allowing options test`() {
        val result = savePreparedInlineMessage(TG_ID) {
            InlineQueryResult.Audio("test", LOREM.AUDIO.url, "testTitle")
        }.sendReturning(bot)
            .shouldFailure() shouldContainInDescription "Bad Request: at least one chat type must be allowed"
    }
}
