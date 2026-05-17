package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.answer.answerGuestQuery
import eu.vendeli.tgbot.types.component.Response
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.types.shouldBeInstanceOf

class AnswerGuestQueryTest : BotTestContext() {
    private val answerPhoto = InlineQueryResult.Photo(
        "testPhoto",
        "https://test.com",
        "https://test.com",
    )

    @Test
    suspend fun `answer guest query test`() {
        val result = answerGuestQuery("test", answerPhoto).sendReq()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()
    }
}
