package eu.vendeli.api.chat

import BotTestContext
import ChatTestingOnlyCondition
import eu.vendeli.tgbot.api.chat.banChatMember
import eu.vendeli.tgbot.api.chat.banChatSenderChat
import eu.vendeli.tgbot.api.chat.unbanChatMember
import eu.vendeli.tgbot.api.chat.unbanChatSenderChat
import eu.vendeli.tgbot.types.internal.onFailure
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.string.shouldContain

@EnabledIf(ChatTestingOnlyCondition::class)
class ChatBanMethodsTest : BotTestContext() {
    @Test
    suspend fun `ban chat member method test`() {
        banChatMember(
            1000L.wrapToUser(),
            CUR_INSTANT.plusMillis(100).epochSecond,
            true
        ).sendReturning(CHAT_ID, bot).onFailure {
            it.description shouldContain "PARTICIPANT_ID_INVALID"
        }.shouldBeNull()
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
        banChatSenderChat(
            1000,
        ).sendReturning(CHAT_ID, bot).onFailure {
            it.description shouldContain "PARTICIPANT_ID_INVALID"
        }.shouldBeNull()
    }

    @Test
    suspend fun `unban chat sender chat method test`() {
        val result = unbanChatSenderChat(1000).sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
    }
}
