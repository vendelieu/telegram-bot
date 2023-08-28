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
        val result = copyMessage(TG_ID, TG_ID, msg!!.messageId).sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            messageId shouldBeGreaterThan msg.messageId
        }
    }

    @Test
    suspend fun `forward message method test`() {
        val msg = message("test").sendReturning(TG_ID, bot).getOrNull()
        val result = forwardMessage(TG_ID, TG_ID, msg!!.messageId).sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            messageId shouldBeGreaterThan msg.messageId
            text shouldBe "test"
        }
    }

    @Test
    suspend fun `delete message method test`() {
        val msg = message("test").sendReturning(TG_ID, bot).getOrNull()
        val result = deleteMessage(msg!!.messageId).sendReturning(TG_ID, bot).shouldSuccess()

        result.shouldBeTrue()
    }
}
