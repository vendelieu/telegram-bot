package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.message.deleteEphemeralMessage
import eu.vendeli.tgbot.api.message.editEphemeralMessageCaption
import eu.vendeli.tgbot.api.message.editEphemeralMessageMedia
import eu.vendeli.tgbot.api.message.editEphemeralMessageReplyMarkup
import eu.vendeli.tgbot.api.message.editEphemeralMessageText
import eu.vendeli.tgbot.api.message.sendMessage
import eu.vendeli.tgbot.types.common.EphemeralMessageParameters
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.types.media.InputRichBlock
import eu.vendeli.tgbot.types.media.InputRichMessage
import eu.vendeli.tgbot.types.msg.toRichText
import eu.vendeli.tgbot.utils.common.toImplicitFile
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

class EphemeralMessageActionsTest : BotTestContext() {
    @Test
    fun `sendMessage supports ephemeral message parameters`() {
        sendMessage("hi")
            .options {
                ephemeralMessageParameters = EphemeralMessageParameters(1L, "query")
            }.apply {
                val ephemeralParameters = parameters["ephemeral_message_parameters"]!!.jsonObject
                ephemeralParameters["receiver_user_id"]?.jsonPrimitive?.long shouldBe 1L
                ephemeralParameters["callback_query_id"]?.jsonPrimitive?.content shouldBe "query"
            }
    }

    @Test
    fun `editEphemeralMessageText wires parameters`() {
        editEphemeralMessageText(1L, 2L) { "new text" }
            .options {
                parseMode = ParseMode.HTML
            }.apply {
                method shouldBe "editEphemeralMessageText"
                parameters["receiver_user_id"]?.jsonPrimitive?.long shouldBe 1L
                parameters["ephemeral_message_id"]?.jsonPrimitive?.long shouldBe 2L
                parameters["text"]?.jsonPrimitive?.content shouldBe "new text"
                parameters["parse_mode"]?.jsonPrimitive?.content shouldBe "HTML"
            }
    }

    @Test
    fun `editEphemeralMessageText accepts rich messages`() {
        editEphemeralMessageText(
            receiverUserId = 1L,
            ephemeralMessageId = 2L,
            richMessage = InputRichMessage(blocks = listOf(InputRichBlock.Paragraph("New text".toRichText()))),
        ).apply {
            parameters["rich_message"]!!.jsonObject["blocks"].toString() shouldBe
                "[{\"type\":\"paragraph\",\"text\":\"New text\"}]"
        }
    }

    @Test
    fun `editEphemeralMessageCaption wires parameters`() {
        editEphemeralMessageCaption(1L, 2L)
            .caption { "new caption" }
            .apply {
                method shouldBe "editEphemeralMessageCaption"
                parameters["receiver_user_id"]?.jsonPrimitive?.long shouldBe 1L
                parameters["ephemeral_message_id"]?.jsonPrimitive?.long shouldBe 2L
                parameters["caption"]?.jsonPrimitive?.content shouldBe "new caption"
            }
    }

    @Test
    fun `editEphemeralMessageMedia wires parameters`() {
        editEphemeralMessageMedia(1L, 2L, InputMedia.Photo("photo_id".toImplicitFile()))
            .apply {
                method shouldBe "editEphemeralMessageMedia"
                parameters["receiver_user_id"]?.jsonPrimitive?.long shouldBe 1L
                parameters["ephemeral_message_id"]?.jsonPrimitive?.long shouldBe 2L
                parameters["media"]!!.jsonObject["type"]?.jsonPrimitive?.content shouldBe "photo"
            }
    }

    @Test
    fun `editEphemeralMessageReplyMarkup wires parameters`() {
        editEphemeralMessageReplyMarkup(1L, 2L).apply {
            method shouldBe "editEphemeralMessageReplyMarkup"
            parameters["receiver_user_id"]?.jsonPrimitive?.long shouldBe 1L
            parameters["ephemeral_message_id"]?.jsonPrimitive?.long shouldBe 2L
        }
    }

    @Test
    fun `deleteEphemeralMessage wires parameters`() {
        deleteEphemeralMessage(1L, 2L).apply {
            method shouldBe "deleteEphemeralMessage"
            parameters["receiver_user_id"]?.jsonPrimitive?.long shouldBe 1L
            parameters["ephemeral_message_id"]?.jsonPrimitive?.long shouldBe 2L
        }
    }
}
