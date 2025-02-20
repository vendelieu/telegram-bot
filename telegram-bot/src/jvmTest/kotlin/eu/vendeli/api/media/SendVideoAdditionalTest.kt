package eu.vendeli.api.media

import BotTestContext
import eu.vendeli.tgbot.api.media.video
import eu.vendeli.tgbot.utils.common.toImplicitFile
import eu.vendeli.utils.LOREM
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import kotlin.time.Duration.Companion.seconds

class SendVideoAdditionalTest : BotTestContext() {
    @Test
    @Suppress("ktlint:standard:function-naming")
    suspend fun `send video with cover and start parameter`() {
        val lorem = LOREM.VIDEO
        val coverImage = RANDOM_PIC ?: return

        val result = video(lorem.bytes)
            .options {
                cover = coverImage.toImplicitFile("image.png", "image/png")
                startTimestamp = 2.seconds
            }.sendReq()
            .shouldSuccess()

        with(result) {
            text.shouldBeNull()
            video.shouldNotBeNull()
            video.fileName.shouldNotBeNull().shouldNotBeBlank()
            video.cover.shouldNotBeNull() shouldHaveSize 1
            video.startTimestamp.shouldNotBeNull() shouldBe 2.seconds
        }
    }
}
