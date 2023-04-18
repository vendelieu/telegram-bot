package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.types.media.Voice
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SerializationIssuesTest : BotTestContext() {
    @Test
    fun `test Voice type for serialization`() {
        val testData = """{"file_id": "test", "file_unique_id": "test1", "duration": 1, "mime_type": "audio/ogg"}"""
        val voice = mapper.runCatching { readValue(testData, Voice::class.java) }

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
}
