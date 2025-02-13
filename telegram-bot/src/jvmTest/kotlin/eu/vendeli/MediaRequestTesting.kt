package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.media.SendMediaGroupAction
import eu.vendeli.tgbot.api.media.mediaGroup
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.toImplicitFile
import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.assertThrows
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

    @org.junit.jupiter.api.Test
    fun `media group requests testing via action`(): Unit = runBlocking {
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
    fun `check mediaGroup action for different types passing`() {
        assertThrows<IllegalArgumentException>("All elements must be of the same specific type") {
            SendMediaGroupAction(
                listOf(
                    InputMedia.Photo("".toImplicitFile()),
                    InputMedia.Audio("".toImplicitFile()),
                ),
            )
        }
    }

    @Test
    fun `check mediaGroup action for unsupported types passing`() {
        assertThrows<IllegalArgumentException>("Only Audio/Document/Photo/Video is possible.") {
            SendMediaGroupAction(
                listOf(
                    InputMedia.Animation("".toImplicitFile()),
                    InputMedia.Photo("".toImplicitFile()),
                ),
            )
        }
    }
}
