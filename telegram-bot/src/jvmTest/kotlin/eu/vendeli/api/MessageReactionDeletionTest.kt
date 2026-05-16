package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.message.deleteAllMessageReactions
import eu.vendeli.tgbot.api.message.deleteMessageReaction
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

class MessageReactionDeletionTest : BotTestContext() {
    @Test
    fun `deleteMessageReaction encodes parameters`() {
        deleteMessageReaction(messageId = 42L).apply {
            parameters["message_id"]?.jsonPrimitive?.long shouldBe 42L
            parameters.containsKey("user_id") shouldBe false
            parameters.containsKey("actor_chat_id") shouldBe false
        }

        deleteMessageReaction(messageId = 42L, userId = 100L, actorChatId = -200L).apply {
            parameters["message_id"]?.jsonPrimitive?.long shouldBe 42L
            parameters["user_id"]?.jsonPrimitive?.long shouldBe 100L
            parameters["actor_chat_id"]?.jsonPrimitive?.long shouldBe -200L
        }
    }

    @Test
    fun `deleteAllMessageReactions encodes parameters`() {
        deleteAllMessageReactions().apply {
            parameters.containsKey("user_id") shouldBe false
            parameters.containsKey("actor_chat_id") shouldBe false
        }

        deleteAllMessageReactions(userId = 100L, actorChatId = -200L).apply {
            parameters["user_id"]?.jsonPrimitive?.long shouldBe 100L
            parameters["actor_chat_id"]?.jsonPrimitive?.long shouldBe -200L
        }
    }
}
