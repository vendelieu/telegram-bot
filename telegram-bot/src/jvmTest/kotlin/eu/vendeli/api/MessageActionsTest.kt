package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.message.copyMessage
import eu.vendeli.tgbot.api.message.copyMessages
import eu.vendeli.tgbot.api.message.deleteMessage
import eu.vendeli.tgbot.api.message.deleteMessages
import eu.vendeli.tgbot.api.message.forwardMessage
import eu.vendeli.tgbot.api.message.forwardMessages
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.api.message.sendMessageDraft
import eu.vendeli.tgbot.api.message.setMessageReaction
import eu.vendeli.tgbot.types.common.EmojiType
import eu.vendeli.tgbot.types.common.ReactionType
import eu.vendeli.tgbot.types.component.getOrNull
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

class MessageActionsTest : BotTestContext() {
    @Test
    suspend fun `copy message method test`() {
        val msg = message("test").sendReq().getOrNull()
        val idResult = copyMessage(TG_ID, msg!!.messageId).sendReq().shouldSuccess()
        val userResult = copyMessage(TG_ID.asUser(), msg.messageId).sendReq().shouldSuccess()
        val chatResult = copyMessage(TG_ID.asChat(), msg.messageId).sendReq().shouldSuccess()

        listOf(idResult, userResult, chatResult).forEach { result ->
            with(result) {
                messageId shouldBeGreaterThan msg.messageId
            }
        }
    }

    @Test
    suspend fun `copy messageS method test`() {
        val msg1 = message("test").sendReq().shouldSuccess()
        val msg2 = message("test2").sendReq().shouldSuccess()

        val idResult = copyMessages(TG_ID, msg1.messageId, msg2.messageId).sendReq().shouldSuccess()
        val userResult =
            copyMessages(TG_ID.asUser(), msg1.messageId, msg2.messageId).sendReq().shouldSuccess()
        val chatResult =
            copyMessages(TG_ID.asChat(), msg1.messageId, msg2.messageId).sendReq().shouldSuccess()

        listOf(idResult, userResult, chatResult).forEach { result ->
            with(result) {
                size shouldBe 2
            }
        }
    }

    @Test
    suspend fun `forward message method test`() {
        val msg = message("test").sendReq().getOrNull()
        val idResult = forwardMessage(TG_ID, msg!!.messageId).sendReq().shouldSuccess()
        val userResult = forwardMessage(TG_ID.asUser(), msg.messageId).sendReq().shouldSuccess()
        val chatResult = forwardMessage(TG_ID.asChat(), msg.messageId).sendReq().shouldSuccess()

        listOf(idResult, userResult, chatResult).forEach { result ->
            with(result) {
                messageId shouldBeGreaterThan msg.messageId
                text shouldBe "test"
            }
        }
    }

    @Test
    suspend fun `forward messageS method test`() {
        val msg1 = message("test").sendReq().shouldSuccess()
        val msg2 = message("test2").sendReq().shouldSuccess()
        val idResult = forwardMessages(TG_ID, msg1.messageId, msg2.messageId).sendReq().shouldSuccess()
        val userResult =
            forwardMessages(TG_ID.asUser(), msg1.messageId, msg2.messageId).sendReq().shouldSuccess()
        val chatResult =
            forwardMessages(TG_ID.asChat(), msg1.messageId, msg2.messageId).sendReq().shouldSuccess()

        listOf(idResult, userResult, chatResult).forEach { result ->
            with(result) {
                size shouldBe 2
            }
        }
    }

    @Test
    suspend fun `delete message method test`() {
        val msg = message("test").sendReq().getOrNull()
        val result = deleteMessage(msg!!.messageId).sendReq().shouldSuccess()

        result.shouldBeTrue()
    }

    @Test
    suspend fun `delete messageS method test`() {
        val msg1 = message("test").sendReq().shouldSuccess()
        val msg2 = message("test2").sendReq().shouldSuccess()

        val result = deleteMessages(msg1.messageId, msg2.messageId).sendReq().shouldSuccess()

        result.shouldBeTrue()
    }

    @Test
    suspend fun `set message reaction method test`() {
        val msg = message("test").sendReq().shouldSuccess()

        val result = setMessageReaction(msg.messageId, ReactionType.Emoji(EmojiType.Eyes))
            .sendReq()
            .shouldSuccess()
        val listingResult = setMessageReaction(msg.messageId) {
            +ReactionType.Emoji("\uD83C\uDF83")
        }.sendReq().shouldSuccess()

        result.shouldBeTrue()
        listingResult.shouldBeTrue()
    }

    @Test
    suspend fun `send message draft method test`() {
        val result = sendMessageDraft(TG_ID, 1, "test draft")
            .sendReq()
            .shouldSuccess()

        result.shouldBeTrue()
    }

    @Test
    suspend fun `send message draft with message thread id method test`() {
        val result = sendMessageDraft(TG_ID, 2, "test draft", 123)
            .sendReq()
            .shouldSuccess()

        result.shouldBeTrue()
    }

    @Test
    suspend fun `send message draft with entities method test`() {
        val result = sendMessageDraft(TG_ID, 3, messageThreadId = null) {
            "test" - bold { " draft" }
        }.sendReq()
            .shouldSuccess()

        result.shouldBeTrue()
    }
}
