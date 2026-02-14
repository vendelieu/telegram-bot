package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.media.mediaGroup
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.api.media.sendMediaGroup
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.component.getOrNull
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.common.toImplicitFile
import eu.vendeli.utils.LOREM
import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import utils.RandomPicResource
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeBytes

class MediaRequestTesting : BotTestContext() {
    @Test
    suspend fun `media requests testing`() {
        val pic = RANDOM_PIC ?: return
        val imageFile = withContext(Dispatchers.IO) {
            Files
                .createTempFile("test", "file")
                .also {
                    it.writeBytes(pic)
                }.toFile()
        }

        val photoString =
            photo { RandomPicResource.RANDOM_PIC_URL }
                .options { parseMode = ParseMode.HTML }
                .caption { "test" }
                .sendReq()
                .getOrNull()
        photoString.shouldNotBeNull()

        val photoBa = photo(pic).sendReq().getOrNull()
        photoBa.shouldNotBeNull()

        val photoFile = photo(imageFile).sendReq().getOrNull()
        photoFile.shouldNotBeNull()
    }

    @Test
    suspend fun `media group requests testing via action`() {
        prepareTestBot()
        val image = classloader.getResource("image.png")?.toURI()
        image.shouldNotBeNull()

        val mediaRequest = mediaGroup(
            InputMedia.Photo(
                File(image).toImplicitFile(),
                caption = "<b>test</b>",
                parseMode = ParseMode.HTML,
            ),
            InputMedia.Photo(File(image).toImplicitFile()),
        ).sendReq().getOrNull()

        mediaRequest.shouldNotBeNull()
        mediaRequest.first().mediaGroupId.shouldNotBeNull()
    }

    @Test
    suspend fun `passing mixed implicit types to media group `() {
        val image = classloader.getResource("image.png")?.toURI()
        image.shouldNotBeNull()

        val imageBytes = classloader.getResource("image.png")?.readBytes() ?: ByteArray(0)
        val mediaRequest = mediaGroup(
            InputMedia.Photo(
                File(image).toImplicitFile(),
                caption = "<b>test</b>",
                parseMode = ParseMode.HTML,
            ),
            InputMedia.Photo(imageBytes.toImplicitFile()),
            InputMedia.Photo(RandomPicResource.RANDOM_PIC_URL.toImplicitFile()),
        ).sendReq().getOrNull()

        mediaRequest.shouldNotBeNull()
        mediaRequest.first().mediaGroupId.shouldNotBeNull()
    }

    @Test
    suspend fun `media group thumbnail test`() {
        val image = classloader.getResource("image.png")?.toURI()
        image.shouldNotBeNull()
        val video = LOREM.VIDEO.file

        val mediaRequest = sendMediaGroup(
            InputMedia.Video(
                video.toImplicitFile("video.mp4", "video/mp4"),
                caption = "<b>test</b>",
                parseMode = ParseMode.HTML,
                thumbnail = File(image).toImplicitFile("thumb.png", "image/png"),
            ),
            InputMedia.Photo(
                File(image).toImplicitFile("thumb.png", "image/png"),
            ),
        ).sendReq().shouldSuccess()

        with(mediaRequest.first()) {
            mediaGroupId.shouldNotBeNull()
            this.video.shouldNotBeNull()
            this.video.thumbnail.shouldNotBeNull()
        }

        with(mediaRequest.get(1)) {
            mediaGroupId.shouldNotBeNull()
            this.photo.shouldNotBeNull()
        }
    }
}
