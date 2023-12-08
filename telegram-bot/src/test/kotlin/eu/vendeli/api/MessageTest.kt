package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.ParseMode
import io.kotest.matchers.shouldBe

class MessageTest : BotTestContext() {
    @Test
    suspend fun `message method test`() {
        val result = message { "test message" - bold { " test" } }.options {
            disableWebPagePreview = true
            allowSendingWithoutReply = true
            parseMode = ParseMode.HTML
            protectContent = true
        }.apply {
            parameters["text"] shouldBe "test message test"
            parameters["disable_web_page_preview"] shouldBe true
            parameters["parse_mode"] shouldBe "HTML"
            parameters["protect_content"] shouldBe true
            parameters["allow_sending_without_reply"] shouldBe true
        }.sendReturning(TG_ID, bot).shouldSuccess()

        result.hasProtectedContent shouldBe true
    }
}
