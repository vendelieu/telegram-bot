package eu.vendeli.api.chat

import BotTestContext
import ChatTestingOnlyCondition
import eu.vendeli.tgbot.api.chat.getChatAdministrators
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive

@EnabledIf(ChatTestingOnlyCondition::class)
class GetChatAdministratorsBotsTest : BotTestContext() {
    @Test
    fun `getChatAdministrators encodes returnBots parameter`() {
        getChatAdministrators(returnBots = true).apply {
            parameters["return_bots"]?.jsonPrimitive?.boolean shouldBe true
        }
        getChatAdministrators(returnBots = false).apply {
            parameters["return_bots"]?.jsonPrimitive?.boolean shouldBe false
        }
        getChatAdministrators().apply {
            parameters.containsKey("return_bots") shouldBe false
        }
    }

    @Test
    suspend fun `get chat administrators with returnBots true`() {
        val result = getChatAdministrators(returnBots = true)
            .sendReturning(CHAT_ID, bot)
            .shouldSuccess()
        result.shouldNotBeEmpty()
    }
}
