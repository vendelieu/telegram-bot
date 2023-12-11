package eu.vendeli.api.media

import BotTestContext
import eu.vendeli.tgbot.api.media.animation
import eu.vendeli.tgbot.api.media.audio
import eu.vendeli.tgbot.api.media.document
import eu.vendeli.tgbot.api.media.sticker
import eu.vendeli.tgbot.api.media.video
import eu.vendeli.tgbot.api.media.videoNote
import eu.vendeli.tgbot.api.media.voice
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.utils.LOREM
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MediaTest : BotTestContext() {
    @Test
    suspend fun `audio method test`() {
        val result = audio { LOREM.AUDIO.data }.sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            audio.shouldNotBeNull()
            audio?.fileName shouldBe "audio.mp3"
            audio?.duration shouldBe 121
        }
    }

    @Test
    suspend fun `animation method test`() {
        val result = animation { LOREM.ANIMATION.data }.sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            animation.shouldNotBeNull()
            animation?.fileName shouldBe "animated-parabola.gif.mp4"
        }
    }

    @Test
    suspend fun `document method test`() {
        val result = document { LOREM.DOCUMENT.data }.sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            document.shouldNotBeNull()
            document?.fileName shouldBe "Lorem_ipsum.pdf"
        }
    }

    @Test
    suspend fun `video method test`() {
        val result = video { LOREM.VIDEO.data }.sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            video.shouldNotBeNull()
            video?.fileName shouldBe "small.mp4"
        }
    }

    @Test
    suspend fun `video note method test`() {
        val file = getExtFile(LOREM.VIDEO_NOTE.data)
        val tempFile = withContext(Dispatchers.IO) {
            File.createTempFile("test", "")
        }
        tempFile.writeBytes(file)
        val inputFile = InputFile(file)

        val bytesResult = videoNote(file).sendReturning(TG_ID, bot).shouldSuccess()
        val fileResult = videoNote(tempFile).sendReturning(TG_ID, bot).shouldSuccess()
        val inputFileResult = videoNote(inputFile).sendReturning(TG_ID, bot).shouldSuccess()

        listOf(bytesResult, fileResult, inputFileResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                text.shouldBeNull()
                videoNote.shouldNotBeNull()
            }
        }
    }

    @Test
    suspend fun `voice method test`() {
        val result = voice(getExtFile(LOREM.VOICE.data)).sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            voice.shouldNotBeNull()
        }
    }

    @Test
    suspend fun `sticker method test`() {
        val result = sticker { LOREM.STICKER.data }.sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            sticker.shouldNotBeNull()
        }
    }
}
