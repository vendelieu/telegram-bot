package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.message.approveSuggestedPost
import eu.vendeli.tgbot.api.message.declineSuggestedPost
import eu.vendeli.tgbot.utils.internal.toJsonElement
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldNotContainKey
import io.kotest.matchers.shouldBe

class SuggestedPostsActionsTest : BotTestContext() {
    @Test
    fun `approveSuggestedPost builder test`() {
        approveSuggestedPost(123L).apply {
            parameters.size shouldBe 1
            parameters.shouldContainKey("message_id")
            parameters["message_id"] shouldBe 123L.toJsonElement()
            parameters.containsKey("send_date").shouldBeFalse()
        }

        approveSuggestedPost(321L, sendDate = 1_700_000_000).apply {
            parameters.size shouldBe 2
            parameters["message_id"] shouldBe 321L.toJsonElement()
            parameters["send_date"] shouldBe 1_700_000_000.toJsonElement()
        }
    }

    @Test
    fun `declineSuggestedPost builder test`() {
        declineSuggestedPost(456L).apply {
            parameters.size shouldBe 1
            parameters.shouldContainKey("message_id")
            parameters.shouldNotContainKey("comment")
            parameters["message_id"] shouldBe 456L.toJsonElement()
        }

        declineSuggestedPost(654L, comment = "not suitable").apply {
            parameters.size shouldBe 2
            parameters["message_id"] shouldBe 654L.toJsonElement()
            parameters["comment"] shouldBe "not suitable".toJsonElement()
        }
    }

    @Test
    suspend fun `approve suggested post request is sent`() {
        // We expect a failure from Telegram (no valid suggested post in this chat),
        // but the call itself must be performed successfully end-to-end.
        approveSuggestedPost(messageId = 1L)
            .sendReq(TG_ID, bot)
            .shouldFailure()
            .description shouldBe "Bad Request: message can't be used in the method"
    }

    @Test
    suspend fun `decline suggested post request is sent`() {
        // We expect a failure from Telegram (no valid suggested post in this chat),
        // but the call itself must be performed successfully end-to-end.
        declineSuggestedPost(messageId = 1L, comment = "test")
            .sendReq(TG_ID, bot)
            .shouldFailure()
            .description shouldBe "Bad Request: message can't be used in the method"
    }
}
