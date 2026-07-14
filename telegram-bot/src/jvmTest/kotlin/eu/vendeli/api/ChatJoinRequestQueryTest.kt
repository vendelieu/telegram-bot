package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.chat.answerChatJoinRequestQuery
import eu.vendeli.tgbot.api.chat.sendChatJoinRequestWebApp
import eu.vendeli.tgbot.types.chat.JoinRequestQueryResult
import eu.vendeli.tgbot.types.component.Response
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.json.jsonPrimitive

class ChatJoinRequestQueryTest : BotTestContext() {
    @Test
    fun `answerChatJoinRequestQuery wires parameters`() {
        answerChatJoinRequestQuery("query-id", JoinRequestQueryResult.Approve).apply {
            method shouldBe "answerChatJoinRequestQuery"
            parameters["chat_join_request_query_id"]?.jsonPrimitive?.content shouldBe "query-id"
            parameters["result"]?.jsonPrimitive?.content shouldBe "approve"
        }
    }

    @Test
    fun `sendChatJoinRequestWebApp wires parameters`() {
        sendChatJoinRequestWebApp("query-id", "https://webapp.example").apply {
            method shouldBe "sendChatJoinRequestWebApp"
            parameters["chat_join_request_query_id"]?.jsonPrimitive?.content shouldBe "query-id"
            parameters["web_app_url"]?.jsonPrimitive?.content shouldBe "https://webapp.example"
        }
    }

    @Test
    suspend fun `answerChatJoinRequestQuery request is sent`() {
        val result = answerChatJoinRequestQuery("test", JoinRequestQueryResult.Queue).sendReq()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()
    }
}
