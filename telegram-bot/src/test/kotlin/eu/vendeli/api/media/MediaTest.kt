package eu.vendeli.api.media

import BotTestContext
import eu.vendeli.fixtures.LOREM
import eu.vendeli.tgbot.api.media.animation
import eu.vendeli.tgbot.api.media.audio
import eu.vendeli.tgbot.api.media.document
import eu.vendeli.tgbot.api.media.sticker
import eu.vendeli.tgbot.api.media.video
import eu.vendeli.tgbot.api.media.videoNote
import eu.vendeli.tgbot.api.media.voice
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class MediaTest : BotTestContext() {
    @Test
    suspend fun `audio method test`() {
        val request = audio { LOREM.AUDIO.data }.sendReturning(TG_ID, bot)

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

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
        val request = animation { LOREM.ANIMATION.data }.sendReturning(TG_ID, bot)

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            animation.shouldNotBeNull()
            animation?.fileName shouldBe "animated-parabola.gif.mp4"
        }
    }

    @Test
    suspend fun `document method test`() {
        val request = document { LOREM.DOCUMENT.data }.sendReturning(TG_ID, bot)

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            document.shouldNotBeNull()
            document?.fileName shouldBe "Lorem_ipsum.pdf"
        }
    }

    @Test
    suspend fun `video method test`() {
        val request = video { LOREM.VIDEO.data }.sendReturning(TG_ID, bot)

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            video.shouldNotBeNull()
            video?.fileName shouldBe "small.mp4"
        }
    }

    @Test
    suspend fun `video note method test`() {
        val request = videoNote(getExtFile(LOREM.VIDEO_NOTE.data)).sendReturning(TG_ID, bot)

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            videoNote.shouldNotBeNull()
        }
    }

    @Test
    suspend fun `voice method test`() {
        val request = voice(getExtFile(LOREM.VOICE.data)).sendReturning(TG_ID, bot)

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            voice.shouldNotBeNull()
        }
    }

    @Test
    suspend fun `sticker method test`() {
        val request = sticker { LOREM.STICKER.data }.sendReturning(TG_ID, bot)

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result) {
            shouldNotBeNull()
            text.shouldBeNull()
            sticker.shouldNotBeNull()
        }
    }
}
