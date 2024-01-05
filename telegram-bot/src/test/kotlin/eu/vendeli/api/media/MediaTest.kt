package eu.vendeli.api.media

import BotTestContext
import eu.vendeli.tgbot.api.media.animation
import eu.vendeli.tgbot.api.media.audio
import eu.vendeli.tgbot.api.media.document
import eu.vendeli.tgbot.api.media.sendAnimation
import eu.vendeli.tgbot.api.media.sendAudio
import eu.vendeli.tgbot.api.media.sendDocument
import eu.vendeli.tgbot.api.media.sendSticker
import eu.vendeli.tgbot.api.media.sendVideo
import eu.vendeli.tgbot.api.media.sendVideoNote
import eu.vendeli.tgbot.api.media.sendVoice
import eu.vendeli.tgbot.api.media.sticker
import eu.vendeli.tgbot.api.media.video
import eu.vendeli.tgbot.api.media.videoNote
import eu.vendeli.tgbot.api.media.voice
import eu.vendeli.utils.LOREM
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank

class MediaTest : BotTestContext() {
    @Test
    suspend fun `audio method test`() {
        val lorem = LOREM.AUDIO
        val bytesResult = audio(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val textResult = sendAudio { bytesResult.audio!!.fileId }.sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = audio(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = audio(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                audio.shouldNotBeNull()
                audio?.fileName.shouldNotBeNull().shouldNotBeBlank()
                audio?.duration shouldBe 121
            }
        }
    }

    @Test
    suspend fun `animation method test`() {
        val lorem = LOREM.ANIMATION
        val textResult = sendAnimation { lorem.url }.sendReturning(TG_ID, bot).shouldSuccess()
        val bytesResult = animation(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = animation(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = animation(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                animation.shouldNotBeNull()
                animation?.fileName.shouldNotBeNull().shouldNotBeBlank()
            }
        }
    }

    @Test
    suspend fun `document method test`() {
        val lorem = LOREM.DOCUMENT
        val textResult = sendDocument { lorem.url }.sendReturning(TG_ID, bot).shouldSuccess()
        val bytesResult = document(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = document(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = document(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                document.shouldNotBeNull()
                document?.fileName.shouldNotBeNull().shouldNotBeBlank()
            }
        }
    }

    @Test
    suspend fun `video method test`() {
        val lorem = LOREM.VIDEO
        val bytesResult = video(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val textResult = sendVideo { bytesResult.video!!.fileId }.sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = video(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = video(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                video.shouldNotBeNull()
                video?.fileName.shouldNotBeNull().shouldNotBeBlank()
            }
        }
    }

    @Test
    suspend fun `video note method test`() {
        val lorem = LOREM.VIDEO_NOTE
        val bytesResult = videoNote(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val textResult = sendVideoNote { bytesResult.videoNote!!.fileId }.sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = videoNote(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = videoNote(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                videoNote.shouldNotBeNull()
            }
        }
    }

    @Test
    suspend fun `voice method test`() {
        val lorem = LOREM.VOICE
        val bytesResult = voice(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val textResult = sendVoice { bytesResult.voice!!.fileId }.sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = voice(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = voice(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                voice.shouldNotBeNull()
            }
        }
    }

    @Test
    suspend fun `sticker method test`() {
        val lorem = LOREM.STICKER
        val bytesResult = sticker(lorem.bytes).sendReturning(TG_ID, bot).shouldSuccess()
        val textResult = sendSticker { bytesResult.sticker!!.fileId }.sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = sticker(lorem.file).sendReturning(TG_ID, bot).shouldSuccess()
        val inputResult = sticker(lorem.inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                sticker.shouldNotBeNull()
            }
        }
    }
}
