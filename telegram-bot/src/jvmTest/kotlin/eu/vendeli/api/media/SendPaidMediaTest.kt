package eu.vendeli.api.media

import BotTestContext
import ChannelTestingOnlyCondition
import eu.vendeli.tgbot.api.media.sendPaidMedia
import eu.vendeli.tgbot.types.media.InputPaidMedia
import eu.vendeli.tgbot.utils.toImplicitFile
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import utils.RandomPicResource.RANDOM_PIC_URL
import java.io.File

@EnabledIf(ChannelTestingOnlyCondition::class)
class SendPaidMediaTest : BotTestContext() {
    @Test
    suspend fun `sendPaidMedia action testing`() {
        val image = classloader.getResource("image.png")?.toURI()
        image.shouldNotBeNull()

        val request = sendPaidMedia(1) {
            +InputPaidMedia.Photo(RANDOM_PIC_URL.toImplicitFile())
            +InputPaidMedia.Photo(File(image).toImplicitFile())
        }.sendReturning(CHANNEL_ID, bot).shouldSuccess()

        request.run {
            paidMedia.shouldNotBeNull()
            paidMedia!!.starCount shouldBe 1
            paidMedia!!.paidMedia.size shouldBe 2
            paidMedia!!.paidMedia.all { it.type == "photo" } shouldBe true
        }
    }
}
