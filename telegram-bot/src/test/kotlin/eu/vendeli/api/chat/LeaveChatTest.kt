package eu.vendeli.api.chat

import BotTestContext
import eu.vendeli.tgbot.api.chat.leaveChat
import eu.vendeli.tgbot.types.internal.Response
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

class LeaveChatTest : BotTestContext() {
    @Test
    suspend fun `leave chat test`() {
        val result = leaveChat().sendAsync(1, bot).await()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()
        result.description shouldContain "chat not found"
    }
}
