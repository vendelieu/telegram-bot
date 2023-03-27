package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class MessageTest : BotTestContext() {
    @Test
    suspend fun `message method test`() {
        val request = message { "test message" }.options {
            disableWebPagePreview = true
            allowSendingWithoutReply = true
            parseMode = ParseMode.HTML
            protectContent = true
        }.apply {
            parameters["text"] shouldBe "test message"
            parameters["disable_web_page_preview"] shouldBe true
            parameters["parse_mode"] shouldBe "HTML"
            parameters["protect_content"] shouldBe true
            parameters["allow_sending_without_reply"] shouldBe true
        }.sendAsync(TG_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        result.hasProtectedContent shouldBe true
    }
}
