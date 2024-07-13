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
import eu.vendeli.tgbot.types.EntityType
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.options.AudioOptions
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.utils.LOREM
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank

class MediaTest : BotTestContext() {
    @Test
    suspend fun `audio method test`() {
        val lorem = LOREM.AUDIO
        val image = classloader.getResource("image.png")?.readBytes()?.toImplicitFile("test.png", "image/png")
        image.shouldNotBeNull()
        val options: AudioOptions.() -> Unit = {
            thumbnail = image
            parseMode = ParseMode.Markdown
        }

        val bytesResult = audio(lorem.bytes)
            .caption { "*test*" }
            .options(options)
            .sendReturning(TG_ID, bot)
            .shouldSuccess()
        val textResult = sendAudio { bytesResult.audio!!.fileId }
            .caption { "*test*" }
            .options(options)
            .sendReturning(TG_ID, bot)
            .shouldSuccess()
        val fileResult = audio(lorem.file)
            .caption { "*test*" }
            .options(options)
            .sendReturning(TG_ID, bot)
            .shouldSuccess()
        val inputResult = audio(lorem.inputFile)
            .caption { "*test*" }
            .options(options)
            .sendReturning(TG_ID, bot)
            .shouldSuccess()

        listOf(textResult, bytesResult, fileResult, inputResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                caption.shouldNotBeNull() shouldBe "test"
                text.shouldBeNull()
                audio.shouldNotBeNull()
                audio!!.fileName.shouldNotBeNull().shouldNotBeBlank()
                audio!!.duration shouldBe 121
                audio!!.thumbnail.shouldNotBeNull()
                audio!!.thumbnail!!.width shouldBe 1
                audio!!.thumbnail!!.height shouldBe 1
                captionEntities.shouldNotBeNull()
                captionEntities!!.first().let {
                    it.offset shouldBe 0
                    it.length shouldBe 4
                    it.type shouldBe EntityType.Bold
                }
            }
        }
    }

    @Test
    suspend fun `animation method test`() {
        val lorem = LOREM.ANIMATION
        val textResult = sendAnimation { lorem.url }
            .caption { "test" }
            .options { parseMode = ParseMode.Markdown }
            .sendReturning(TG_ID, bot)
            .shouldSuccess()
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
                document!!.fileName.shouldNotBeNull().shouldNotBeBlank()
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
