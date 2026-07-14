package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.message.richMessage
import eu.vendeli.tgbot.api.message.sendRichMessage
import eu.vendeli.tgbot.api.message.sendRichMessageDraft
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.types.media.InputRichBlock
import eu.vendeli.tgbot.types.media.InputRichMessage
import eu.vendeli.tgbot.types.media.InputRichMessageMedia
import eu.vendeli.tgbot.types.msg.RichBlock
import eu.vendeli.tgbot.types.msg.RichMessage
import eu.vendeli.tgbot.types.msg.RichText
import eu.vendeli.tgbot.types.msg.toRichText
import eu.vendeli.tgbot.utils.common.serde
import eu.vendeli.tgbot.utils.common.toImplicitFile
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class RichMessageTest : BotTestContext() {
    @Test
    fun `richText plain form is serialized as bare string`() {
        val json = serde.encodeToString(RichText.serializer(), "hello".toRichText())
        json shouldBe "\"hello\""
    }

    @Test
    fun `richText chunks form is serialized as array`() {
        val text = listOf("plain".toRichText(), RichText.Bold("bold".toRichText())).toRichText()
        val json = serde.encodeToString(RichText.serializer(), text)
        json shouldBe """["plain",{"type":"bold","text":"bold"}]"""
    }

    @Test
    fun `richText typed form is serialized with type discriminator`() {
        val json = serde.encodeToString(
            RichText.serializer(),
            RichText.Url("link".toRichText(), "https://example.com"),
        )
        json shouldBe """{"type":"url","text":"link","url":"https://example.com"}"""
    }

    @Test
    fun `richText forms are deserialized back to matching subtypes`() {
        serde.decodeFromString(RichText.serializer(), "\"hi\"") shouldBe RichText.Plain("hi")

        val chunks = serde.decodeFromString(RichText.serializer(), """["a",{"type":"italic","text":"b"}]""")
        chunks shouldBe RichText.Chunks(RichText.Plain("a"), RichText.Italic(RichText.Plain("b")))

        val dateTime = serde.decodeFromString(
            RichText.serializer(),
            """{"type":"date_time","text":"now","unix_time":123,"date_time_format":"HH:mm"}""",
        )
        dateTime shouldBe RichText.DateTime(RichText.Plain("now"), 123, "HH:mm")
    }

    @Test
    fun `richMessage blocks are deserialized polymorphically`() {
        val payload = """
            {"blocks":[
                {"type":"heading","text":"Title","size":1},
                {"type":"paragraph","text":"Body"},
                {"type":"divider"}
            ],"is_rtl":true}
        """.trimIndent()
        val message = serde.decodeFromString(RichMessage.serializer(), payload)

        message.isRtl shouldBe true
        message.blocks[0].shouldBeTypeOf<RichBlock.SectionHeading>().size shouldBe 1
        message.blocks[1].shouldBeTypeOf<RichBlock.Paragraph>().text shouldBe RichText.Plain("Body")
        message.blocks[2] shouldBe RichBlock.Divider
    }

    @Test
    fun `sendRichMessage wires rich_message parameter`() {
        sendRichMessage {
            blocks = listOf(
                InputRichBlock.Paragraph("hello".toRichText()),
            )
            isRtl = true
        }.apply {
            method shouldBe "sendRichMessage"
            val richMessage = parameters["rich_message"]!!.jsonObject
            richMessage["is_rtl"]?.jsonPrimitive?.content shouldBe "true"
            richMessage["blocks"].toString() shouldContain "\"type\":\"paragraph\""
        }
    }

    @Test
    fun `sendRichMessage html content with media is wired`() {
        val media = InputRichMessageMedia(
            id = "pic-1",
            media = InputMedia.Photo("photo_id".toImplicitFile()),
        )
        richMessage(
            InputRichMessage(html = "<img src='tg://photo?id=pic-1'/>", media = listOf(media)),
        ).apply {
            val richMessage = parameters["rich_message"]!!.jsonObject
            richMessage["html"]?.jsonPrimitive?.content shouldContain "tg://photo?id=pic-1"
            richMessage["media"].toString() shouldContain "\"id\":\"pic-1\""
            richMessage["media"].toString() shouldContain "\"type\":\"photo\""
        }
    }

    @Test
    fun `sendRichMessageDraft wires draft_id and rich_message`() {
        sendRichMessageDraft(42) {
            markdown = "*partial*"
        }.options {
            messageThreadId = 7
        }.apply {
            method shouldBe "sendRichMessageDraft"
            parameters["draft_id"]?.jsonPrimitive?.int shouldBe 42
            parameters["rich_message"]!!.jsonObject["markdown"]?.jsonPrimitive?.content shouldBe "*partial*"
            parameters["message_thread_id"]?.jsonPrimitive?.int shouldBe 7
        }
    }
}
