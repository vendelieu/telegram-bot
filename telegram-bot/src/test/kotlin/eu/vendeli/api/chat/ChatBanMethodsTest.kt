package eu.vendeli.api.chat

import BotTestContext
import eu.vendeli.tgbot.api.chat.banChatMember
import eu.vendeli.tgbot.api.chat.banChatSenderChat
import eu.vendeli.tgbot.api.chat.unbanChatMember
import eu.vendeli.tgbot.api.chat.unbanChatSenderChat
import io.kotest.matchers.booleans.shouldBeTrue

class ChatBanMethodsTest : BotTestContext() {
    @Test
    suspend fun `ban chat member method test`() {
        val result = banChatMember(
            TG_ID,
            CUR_INSTANT.plusMillis(100).epochSecond,
        ).sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
        unbanChatMember(TG_ID).sendReturning(CHAT_ID, bot).shouldSuccess()
    }

    @Test
    suspend fun `unban chat member method test`() {
        val result = unbanChatMember(
            TG_ID,
            onlyIfBanned = true,
        ).sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `ban unban sender chat chat member method test`() {
        val result = banChatSenderChat(
            TG_ID,
        ).sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
        unbanChatSenderChat(TG_ID).sendReturning(CHAT_ID, bot).shouldSuccess()
    }
}
