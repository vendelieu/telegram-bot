package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.msg.MaybeInaccessibleMessage
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.keyboard.ForceReply
import eu.vendeli.tgbot.types.media.Voice
import eu.vendeli.tgbot.utils.serde
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString

class SerdeIssuesTest : BotTestContext() {
    @Test
    fun `Voice type deserialization`() {
        val testData = """{"file_id": "test", "file_unique_id": "test1", "duration": 1, "mime_type": "audio/ogg"}"""
        val voice = serde.runCatching { decodeFromString(Voice.serializer(), testData) }

        voice.isSuccess shouldBe true
        voice.getOrNull() shouldNotBe null

        voice.getOrNull()?.run {
            fileId shouldBe "test"
            fileUniqueId shouldBe "test1"
            duration shouldBe 1
            mimeType shouldNotBe null
            mimeType shouldBe "audio/ogg"
        }
    }

    @Test
    fun `reply markup parameter wrong serialization`() {
        val markup = ForceReply(inputFieldPlaceholder = "test")
        val serializedMarkup = serde.encodeToString(markup)
        serializedMarkup shouldContain "input_field_placeholder"
    }

    @Test
    fun `MaybeInaccessibleMessage serde test`() {
        val msg = Message(1, chat = Chat(2, ChatType.Group), date = Instant.fromEpochMilliseconds(0))
        val serializedMessage = serde.encodeToString(msg)
        shouldNotThrowAny { serde.decodeFromString(MaybeInaccessibleMessage.serializer(), serializedMessage) }
            .shouldBeTypeOf<MaybeInaccessibleMessage.InaccessibleMessage>()

        val msgWithDate = Message(1, chat = Chat(2, ChatType.Group), date = Clock.System.now())
        val serializedDateMessage = serde.encodeToString(msgWithDate)
        shouldNotThrowAny {
            serde.decodeFromString(MaybeInaccessibleMessage.serializer(), serializedDateMessage)
        }.shouldBeTypeOf<Message>()
    }
}
