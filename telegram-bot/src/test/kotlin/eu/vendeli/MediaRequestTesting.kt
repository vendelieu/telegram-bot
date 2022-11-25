package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.media.SendMediaGroupAction
import eu.vendeli.tgbot.api.media.mediaGroup
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.interfaces.sendAsync
import eu.vendeli.tgbot.types.InputMedia
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.utils.makeBunchMediaRequestAsync
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeBytes

class MediaRequestTesting : BotTestContext() {
    private var classloader = Thread.currentThread().contextClassLoader

    @Test
    fun `media requests testing`(): Unit = runBlocking {
        val image =
            bot.httpClient.get("https://storage.myseldon.com/news-pict-fe/FEB5B36049C75ADDFF40C00221D2D9D5").readBytes()

        val imageFile = withContext(Dispatchers.IO) {
            Files.createTempFile("test", "file").also {
                it.writeBytes(image)
            }.toFile()
        }

        val photoString = photo {
            "https://storage.myseldon.com/news-pict-fe/FEB5B36049C75ADDFF40C00221D2D9D5"
        }.options { parseMode = ParseMode.HTML }.caption { "test" }
            .sendAsync(System.getenv("TELEGRAM_ID").toLong().toLong(), bot).await().getOrNull()
        assertNotNull(photoString)

        val photoBa = photo(image).options { parseMode = ParseMode.HTML }.caption { "<i>test</i>" }
            .sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await().getOrNull()
        assertNotNull(photoBa)

        val photoFile = photo(imageFile).caption { "<b>test</b>" }.options { parseMode = ParseMode.HTML }
            .sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await().getOrNull()
        assertNotNull(photoFile)
    }

    @Test
    fun `media group requests testing`(): Unit = runBlocking {
        val image = classloader.getResource("image.png")?.readBytes() ?: ByteArray(0)

        @Suppress("UNCHECKED_CAST")
        val mediaGroupReq = bot.makeBunchMediaRequestAsync(
            TgMethod("sendMediaGroup"), mapOf(
                "test.png" to image,
                "test2.png" to image,
                "test3.png" to image,
                "test4.png" to image,
            ), parameters = mapOf(
                "chat_id" to System.getenv("TELEGRAM_ID"),
                "media" to listOf(
                    mapOf("type" to "photo", "media" to "attach://test.png"),
                    mapOf("type" to "photo", "media" to "attach://test2.png"),
                    mapOf("type" to "photo", "media" to "attach://test3.png"),
                    mapOf("type" to "photo", "media" to "attach://test4.png"),
                )
            ), ContentType.Image.PNG, List::class.java, Message::class.java
        ).await().getOrNull() as? List<Message>

        assertNotNull(mediaGroupReq)
        assertNotNull(mediaGroupReq?.first()?.mediaGroupId)
    }

    @Test
    fun `media group requests testing via action`(): Unit = runBlocking {
        val image = classloader.getResource("image.png")?.toURI() ?: throw RuntimeException()
        val mediaRequest = mediaGroup(
            InputMedia.Photo(ImplicitFile.FromFile(File(image)), caption = "<b>test</b>", parseMode = ParseMode.HTML),
            InputMedia.Photo(ImplicitFile.FromFile(File(image))),
        ).sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await().getOrNull()

        assertNotNull(mediaRequest)
        assertNotNull(mediaRequest?.first()?.mediaGroupId)
    }

    @Test
    fun `passing mixed implicit types to media group `(): Unit = runBlocking {
        val image = classloader.getResource("image.png")?.toURI() ?: throw RuntimeException()
        val imageBytes = classloader.getResource("image.png")?.readBytes() ?: ByteArray(0)
        val mediaRequest = mediaGroup(
            InputMedia.Photo(ImplicitFile.FromFile(File(image)), caption = "<b>test</b>", parseMode = ParseMode.HTML),
            InputMedia.Photo(ImplicitFile.FromByteArray(imageBytes)),
            InputMedia.Photo(ImplicitFile.FromString("https://storage.myseldon.com/news-pict-fe/FEB5B36049C75ADDFF40C00221D2D9D5")),
        ).sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await().getOrNull()

        assertNotNull(mediaRequest)
        assertNotNull(mediaRequest?.first()?.mediaGroupId)
    }

    @Test
    fun `check mediaGroup action for different types passing`(): Unit = runBlocking {
        assertThrows<IllegalArgumentException>("All elements must be of the same specific type") {
            SendMediaGroupAction(
                InputMedia.Photo(ImplicitFile.FromString("")),
                InputMedia.Audio(ImplicitFile.FromString(""))
            )
        }
    }

    @Test
    fun `check mediaGroup action for unsupported types passing`(): Unit = runBlocking {
        assertThrows<IllegalArgumentException>("Only Audio/Document/Photo/Video is possible.") {
            SendMediaGroupAction(
                InputMedia.Animation(ImplicitFile.FromString("")),
                InputMedia.Photo(ImplicitFile.FromString(""))
            )
        }
    }
}
