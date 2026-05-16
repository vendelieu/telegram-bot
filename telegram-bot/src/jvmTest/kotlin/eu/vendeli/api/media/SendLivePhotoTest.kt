package eu.vendeli.api.media

import BotTestContext
import eu.vendeli.tgbot.api.message.livePhoto
import eu.vendeli.tgbot.api.message.sendLivePhoto
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.utils.LOREM
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive

class SendLivePhotoTest : BotTestContext() {
    @Test
    fun `sendLivePhoto wires media and photo parameters`() {
        sendLivePhoto(livePhoto = "video_id_42", photo = "photo_id_84")
            .options {
                parseMode = ParseMode.HTML
                hasSpoiler = true
                showCaptionAboveMedia = true
            }
            .apply {
                parameters["live_photo"]?.jsonPrimitive?.content shouldBe "video_id_42"
                parameters["photo"]?.jsonPrimitive?.content shouldBe "photo_id_84"
                parameters["parse_mode"]?.jsonPrimitive?.content shouldBe "HTML"
                parameters["has_spoiler"]?.jsonPrimitive?.boolean shouldBe true
                parameters["show_caption_above_media"]?.jsonPrimitive?.boolean shouldBe true
            }
    }

    @Test
    fun `livePhoto factories accept InputFile inputs`() {
        livePhoto(LOREM.VIDEO.inputFile, LOREM.VIDEO.inputFile).apply {
            parameters.containsKey("live_photo") shouldBe true
            parameters.containsKey("photo") shouldBe true
        }
    }
}
