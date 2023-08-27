package eu.vendeli.api.chat

import BotTestContext
import eu.vendeli.tgbot.api.chat.getChat
import eu.vendeli.tgbot.types.chat.ChatType
import io.kotest.matchers.shouldBe

class ChatBanMethodsTest : BotTestContext() {
    // TODO BAN UNBAN
    @Test
    suspend fun `get chat method test`() {
        val result = getChat().sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            id shouldBe TG_ID
            type shouldBe ChatType.Private
        }
    }
}
