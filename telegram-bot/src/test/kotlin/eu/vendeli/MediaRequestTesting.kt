package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.media.SendMediaGroupAction
import eu.vendeli.tgbot.api.media.mediaGroup
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.makeBunchMediaRequestAsync
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeBytes

class MediaRequestTesting : BotTestContext() {
    @Test
    suspend fun `media requests testing`() {
        val imageFile = withContext(Dispatchers.IO) {
            Files.createTempFile("test", "file").also {
                it.writeBytes(RANDOM_PIC)
            }.toFile()
        }

        val photoString = photo { RANDOM_PIC_URL }.options { parseMode = ParseMode.HTML }.caption { "test" }
            .sendReturning(TG_ID, bot).getOrNull()
        photoString.shouldNotBeNull()

        val photoBa = photo(RANDOM_PIC).options { parseMode = ParseMode.HTML }.caption { "<i>test</i>" }
            .sendReturning(TG_ID, bot).getOrNull()
        photoBa.shouldNotBeNull()

        val photoFile = photo(imageFile).caption { "<b>test</b>" }.options { parseMode = ParseMode.HTML }
            .sendReturning(TG_ID, bot).getOrNull()
        photoFile.shouldNotBeNull()
    }

    @Test
    suspend fun `media group requests testing`() {
        val image = classloader.getResource("image.png")?.readBytes()
        image.shouldNotBeNull()

        @Suppress("UNCHECKED_CAST")
        val mediaGroupReq = bot.makeBunchMediaRequestAsync(
            TgMethod("sendMediaGroup"),
            mapOf(
                "test.png" to image,
                "test2.png" to image,
                "test3.png" to image,
                "test4.png" to image,
            ),
            parameters = mapOf(
                "chat_id" to TG_ID,
                "media" to listOf(
                    mapOf("type" to "photo", "media" to "attach://test.png"),
                    mapOf("type" to "photo", "media" to "attach://test2.png"),
                    mapOf("type" to "photo", "media" to "attach://test3.png"),
                    mapOf("type" to "photo", "media" to "attach://test4.png"),
                ),
            ),
            ContentType.Image.PNG,
            List::class.java,
            Message::class.java,
        ).await().getOrNull() as? List<Message>

        mediaGroupReq.shouldNotBeNull()
        mediaGroupReq.first().mediaGroupId.shouldNotBeNull()
    }

    @Test
    suspend fun `media group requests testing via action`() {
        val image = classloader.getResource("image.png")?.toURI()
        image.shouldNotBeNull()

        val mediaRequest = mediaGroup(
            InputMedia.Photo(
                ImplicitFile.InpFile(File(image).toInputFile()),
                caption = "<b>test</b>",
                parseMode = ParseMode.HTML,
            ),
            InputMedia.Photo(ImplicitFile.InpFile(File(image).toInputFile())),
        ).sendReturning(TG_ID, bot).getOrNull()

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
                ImplicitFile.InpFile(File(image).toInputFile()),
                caption = "<b>test</b>",
                parseMode = ParseMode.HTML,
            ),
            InputMedia.Photo(ImplicitFile.InpFile(imageBytes.toInputFile())),
            InputMedia.Photo(ImplicitFile.Str(RANDOM_PIC_URL)),
        ).sendReturning(TG_ID, bot).getOrNull()

        mediaRequest.shouldNotBeNull()
        mediaRequest.first().mediaGroupId.shouldNotBeNull()
    }

    @Test
    fun `check mediaGroup action for different types passing`() {
        assertThrows<IllegalArgumentException>("All elements must be of the same specific type") {
            SendMediaGroupAction(
                InputMedia.Photo(ImplicitFile.Str("")),
                InputMedia.Audio(ImplicitFile.Str("")),
            )
        }
    }

    @Test
    fun `check mediaGroup action for unsupported types passing`() {
        assertThrows<IllegalArgumentException>("Only Audio/Document/Photo/Video is possible.") {
            SendMediaGroupAction(
                InputMedia.Animation(ImplicitFile.Str("")),
                InputMedia.Photo(ImplicitFile.Str("")),
            )
        }
    }
}
