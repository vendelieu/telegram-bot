package eu.vendeli.api.chat

import BotTestContext
import eu.vendeli.tgbot.api.chat.approveChatJoinRequest
import eu.vendeli.tgbot.api.chat.chatAction
import eu.vendeli.tgbot.api.chat.declineChatJoinRequest
import eu.vendeli.tgbot.api.chat.pinChatMessage
import eu.vendeli.tgbot.api.chat.restrictChatMember
import eu.vendeli.tgbot.api.chat.unpinAllChatMessage
import eu.vendeli.tgbot.api.chat.unpinChatMessage
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.chat.ChatAction
import eu.vendeli.tgbot.types.internal.onFailure
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.string.shouldContain

class ChatGeneralMethodsTest : BotTestContext() {
    @Test
    suspend fun `chat action method test`() {
        var result = chatAction { ChatAction.ChooseSticker }.sendReturning(TG_ID, bot).shouldSuccess()
        result.shouldBeTrue()

        result = chatAction(ChatAction.RecordVideo).sendReturning(TG_ID, bot).shouldSuccess()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `unpin all chat message method test`() {
        val result = unpinAllChatMessage().sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `pin chat message method test`() {
        val message = message("test").sendReturning(CHAT_ID, bot).shouldSuccess()
        val result = pinChatMessage(message.messageId).sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `unpin chat message method test`() {
        val message = message("test").sendReturning(CHAT_ID, bot).shouldSuccess()
        pinChatMessage(message.messageId).sendReturning(CHAT_ID, bot).shouldSuccess()
        val result = unpinChatMessage(message.messageId).sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `approve chat join request method test`() {
        val result = approveChatJoinRequest(TG_ID).sendReturning(CHAT_ID, bot)
        result.onFailure {
            it.description shouldContain "USER_ALREADY_PARTICIPANT"
        }.shouldBeNull()
    }

    @Test
    suspend fun `decline chat join request method test`() {
        val result = declineChatJoinRequest(TG_ID).sendReturning(CHAT_ID, bot)
        result.onFailure {
            it.description shouldContain "HIDE_REQUESTER_MISSING"
        }.shouldBeNull()
    }

    @Test
    suspend fun `restrict chat member method test`() {
        val result = restrictChatMember(TG_ID) {
            canChangeInfo = false
        }.sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
    }
}
