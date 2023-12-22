package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.copyMessage
import eu.vendeli.tgbot.api.deleteMessage
import eu.vendeli.tgbot.api.forwardMessage
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.internal.getOrNull
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

class MessageActionsTest : BotTestContext() {
    @Test
    suspend fun `copy message method test`() {
        val msg = message("test").sendReturning(TG_ID, bot).getOrNull()
        val idResult = copyMessage(TG_ID, msg!!.messageId).sendReturning(TG_ID, bot).shouldSuccess()
        val userResult = copyMessage(TG_ID.asUser(), msg.messageId).sendReturning(TG_ID, bot).shouldSuccess()
        val chatResult = copyMessage(TG_ID.asChat(), msg.messageId).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(idResult, userResult, chatResult).forEach { result ->
            with(result) {
                messageId shouldBeGreaterThan msg.messageId
            }
        }
    }

    @Test
    suspend fun `forward message method test`() {
        val msg = message("test").sendReturning(TG_ID, bot).getOrNull()
        val idResult = forwardMessage(TG_ID, msg!!.messageId).sendReturning(TG_ID, bot).shouldSuccess()
        val userResult = forwardMessage(TG_ID.asUser(), msg.messageId).sendReturning(TG_ID, bot).shouldSuccess()
        val chatResult = forwardMessage(TG_ID.asChat(), msg.messageId).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(idResult, userResult, chatResult).forEach { result->
            with(result) {
                messageId shouldBeGreaterThan msg.messageId
                text shouldBe "test"
            }
        }
    }

    @Test
    suspend fun `delete message method test`() {
        val msg = message("test").sendReturning(TG_ID, bot).getOrNull()
        val result = deleteMessage(msg!!.messageId).sendReturning(TG_ID, bot).shouldSuccess()

        result.shouldBeTrue()
    }
}
