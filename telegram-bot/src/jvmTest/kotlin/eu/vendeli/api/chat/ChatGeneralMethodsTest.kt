package eu.vendeli.api.chat

import BotTestContext
import ChatTestingOnlyCondition
import eu.vendeli.tgbot.api.chat.approveChatJoinRequest
import eu.vendeli.tgbot.api.chat.chatAction
import eu.vendeli.tgbot.api.chat.declineChatJoinRequest
import eu.vendeli.tgbot.api.chat.pinChatMessage
import eu.vendeli.tgbot.api.chat.restrictChatMember
import eu.vendeli.tgbot.api.chat.unpinAllChatMessages
import eu.vendeli.tgbot.api.chat.unpinChatMessage
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.chat.ChatAction
import eu.vendeli.tgbot.types.internal.onFailure
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.string.shouldContain
import kotlin.time.Duration.Companion.milliseconds

@EnabledIf(ChatTestingOnlyCondition::class)
class ChatGeneralMethodsTest : BotTestContext() {
    @Test
    suspend fun `chat action method test`() {
        var result = chatAction { ChatAction.ChooseSticker }.sendReq().shouldSuccess()
        result.shouldBeTrue()

        result = chatAction(ChatAction.RecordVideo).sendReq().shouldSuccess()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `unpin all chat message method test`() {
        val result = unpinAllChatMessages().sendReturning(CHAT_ID, bot).shouldSuccess()
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
        result
            .onFailure {
                it.description shouldContain "USER_ALREADY_PARTICIPANT"
            }.shouldBeNull()
    }

    @Test
    suspend fun `decline chat join request method test`() {
        val result = declineChatJoinRequest(TG_ID).sendReturning(CHAT_ID, bot)
        result
            .onFailure {
                it.description shouldContain "HIDE_REQUESTER_MISSING"
            }.shouldBeNull()
    }

    @Test
    suspend fun `restrict chat member method test`() {
        val result = restrictChatMember(
            user = TG_ID.asUser(),
            untilDate = CUR_INSTANT.plus(10000.milliseconds),
            true,
        ) {
            canChangeInfo = false
        }.sendReturning(CHAT_ID, bot).shouldSuccess()

        result.shouldBeTrue()

        restrictChatMember(TG_ID) {
            canChangeInfo = true
        }.sendAsync(CHAT_ID, bot).shouldSuccess()
    }
}
