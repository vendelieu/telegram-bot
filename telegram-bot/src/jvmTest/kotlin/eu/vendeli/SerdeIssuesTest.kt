package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatMember
import eu.vendeli.tgbot.types.chat.ChatMemberUpdated
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.types.keyboard.ForceReply
import eu.vendeli.tgbot.types.media.Voice
import eu.vendeli.tgbot.types.msg.MaybeInaccessibleMessage
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.msg.MessageOrigin
import eu.vendeli.tgbot.utils.serde
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.equality.shouldBeEqualUsingFields
import io.kotest.matchers.nulls.shouldNotBeNull
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

        voice.getOrNull() shouldNotBe null
        voice.isSuccess shouldBe true

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

    @Test
    fun `InlineQueryResult serde test`() {
        val json = """{"type":"photo","id":"test","photo_url":"testUrl","thumbnail_url":"url"}"""
        val result = serde.decodeFromString(InlineQueryResult.serializer(), json)

        result::class shouldBe InlineQueryResult.Photo::class
        result.type shouldBe "photo"
    }

    @Test
    fun `ChatMember serde test`() {
        val member = ChatMember.Member(DUMB_USER)
        val result = serde.runCatching { encodeToString(ChatMember.serializer(), member) }.getOrNull().shouldNotBeNull()

        result shouldContain "\"id\":1,\"is_bot\":false,\"first_name\":\"Test\""
        result shouldContain "\"status\":\"member\""

        val json = "{\"status\":\"member\",\"user\":{\"id\":1,\"is_bot\":false,\"first_name\":\"\"}}"
        serde.runCatching { decodeFromString<ChatMember.Member>(json) }.getOrNull().shouldNotBeNull()
    }

    @Test
    fun `ChatMemberUpdated serde test`() {
        val instant = CUR_INSTANT

        val oldChatMember = ChatMember.Member(DUMB_USER)
        val newChatMember = ChatMember.Banned(DUMB_USER, instant)
        val member = ChatMemberUpdated(DUMB_CHAT, DUMB_USER, instant, oldChatMember, newChatMember)

        val result = serde.run { encodeToString(member) }.shouldNotBeNull()

        result shouldContain "{\"id\":1,\"is_bot\":false,\"first_name\":\"Test\"}" // DUMB_USER
        result shouldContain "{\"id\":-1,\"type\":\"group\",\"title\":\"test\",\"full_name\":\"\"}" // DUMB_CHAT

        result shouldContain "\"status\":\"member\"" // ChatMember.Member
        result shouldContain "\"status\":\"kicked\"" // ChatMember.Banned

        val json = "{\"chat\":{\"id\":-1,\"type\":\"group\",\"title\":\"test\",\"full_name\":\"\"}," +
            "\"from\":{\"id\":1,\"is_bot\":false,\"first_name\":\"Test\"},\"date\":1733530043," +
            "\"old_chat_member\":{\"status\":\"member\",\"user\":{\"id\":1,\"is_bot\":false," +
            "\"first_name\":\"Test\"}},\"new_chat_member\":{\"status\":\"kicked\"," +
            "\"user\":{\"id\":1,\"is_bot\":false,\"first_name\":\"Test\"},\"until_date\":1733530043}}"

        serde.runCatching { decodeFromString<ChatMemberUpdated>(json) }.getOrNull().shouldNotBeNull().run {
            this.oldChatMember shouldBeEqualUsingFields {
                excludedProperties = listOf(ChatMember.Member::untilDate)
                oldChatMember
            }

            this.newChatMember shouldBeEqualUsingFields {
                excludedProperties = listOf(ChatMember.Banned::untilDate)
                newChatMember
            }
        }
    }

    @Test
    fun `MessageOriginUserOrigin serde test`() {
        val instant = CUR_INSTANT

        serde.encodeToString(
            MessageOrigin.serializer(),
            MessageOrigin.UserOrigin(instant, DUMB_USER),
        ) shouldContain "\"type\":\"user\""

        serde
            .decodeFromString(
                MessageOrigin.serializer(),
                "{\"type\":\"user\",\"date\":1733529723,\"sender_user\":{\"id\":1,\"is_bot\":false,\"first_name\":\"Test\"}}",
            ).type shouldBe "user"
    }
}
