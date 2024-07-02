package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.LinkPreviewOptions
import eu.vendeli.tgbot.types.ParseMode
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class MessageTest : BotTestContext() {
    @Test
    suspend fun `message method test`() {
        val result = message { "test message" - bold { " test" } }
            .options {
                linkPreviewOptions = LinkPreviewOptions(true)
                parseMode = ParseMode.HTML
                protectContent = true
            }.apply {
                parameters["text"]?.jsonPrimitive?.content shouldBe "test message test"
                parameters["link_preview_options"]
                    ?.jsonObject
                    ?.get("is_disabled")
                    ?.jsonPrimitive
                    ?.boolean shouldBe true
                parameters["parse_mode"]?.jsonPrimitive?.content shouldBe "HTML"
                parameters["protect_content"]?.jsonPrimitive?.boolean shouldBe true
            }.sendReturning(TG_ID, bot)
            .shouldSuccess()

        result.hasProtectedContent shouldBe true
    }
}
