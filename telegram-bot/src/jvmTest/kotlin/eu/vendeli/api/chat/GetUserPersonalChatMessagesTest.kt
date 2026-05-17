package eu.vendeli.api.chat

import BotTestContext
import eu.vendeli.tgbot.api.chat.getUserPersonalChatMessages
import eu.vendeli.tgbot.types.component.Response
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

class GetUserPersonalChatMessagesTest : BotTestContext() {
    @Test
    fun `getUserPersonalChatMessages encodes parameters`() {
        getUserPersonalChatMessages(userId = 42L, limit = 5).apply {
            parameters["user_id"]?.jsonPrimitive?.long shouldBe 42L
            parameters["limit"]?.jsonPrimitive?.int shouldBe 5
        }
    }

    @Test
    suspend fun `getUserPersonalChatMessages fails for unknown user`() {
        val result = getUserPersonalChatMessages(userId = 1L, limit = 1).sendReq()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()
    }
}
