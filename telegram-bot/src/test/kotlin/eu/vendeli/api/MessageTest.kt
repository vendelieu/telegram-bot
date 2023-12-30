package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.LinkPreviewOptions
import eu.vendeli.tgbot.types.ParseMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class MessageTest : BotTestContext() {
    @Test
    suspend fun `message method test`() {
        val result = message { "test message" - bold { " test" } }.options {
            linkPreviewOptions = LinkPreviewOptions(true)
            parseMode = ParseMode.HTML
            protectContent = true
        }.apply {
            parameters["text"] shouldBe "test message test"
            parameters["link_preview_options"].shouldBeInstanceOf<Map<String, Boolean>>()["is_disabled"] shouldBe true
            parameters["parse_mode"] shouldBe "HTML"
            parameters["protect_content"] shouldBe true
        }.sendReturning(TG_ID, bot).shouldSuccess()

        result.hasProtectedContent shouldBe true
    }
}
