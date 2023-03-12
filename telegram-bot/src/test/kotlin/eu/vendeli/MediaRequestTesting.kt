package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.media.SendMediaGroupAction
import eu.vendeli.tgbot.api.media.mediaGroup
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.makeBunchMediaRequestAsync
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
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
        message { "test" - bold { "test2" } }.also { print(it.parameters) }

        val image = bot.httpClient.get(
            "https://storage.myseldon.com/news-pict-fe/FEB5B36049C75ADDFF40C00221D2D9D5",
        ).readBytes()

        val imageFile = withContext(Dispatchers.IO) {
            Files.createTempFile("test", "file").also {
                it.writeBytes(image)
            }.toFile()
        }

        val photoString = photo {
            "https://storage.myseldon.com/news-pict-fe/FEB5B36049C75ADDFF40C00221D2D9D5"
        }.options { parseMode = ParseMode.HTML }.caption { "test" }
            .sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await().getOrNull()
        photoString.shouldNotBeNull()

        val photoBa = photo(image).options { parseMode = ParseMode.HTML }.caption { "<i>test</i>" }
            .sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await().getOrNull()
        photoBa.shouldNotBeNull()

        val photoFile = photo(imageFile).caption { "<b>test</b>" }.options { parseMode = ParseMode.HTML }
            .sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await().getOrNull()
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
                "chat_id" to System.getenv("TELEGRAM_ID").toLong(),
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
    suspend fun `new sticker set bunch stickers sending`() {
        val image = classloader.getResource("image.png")?.readBytes()
        image.shouldNotBeNull()

//        val response: Deferred<Response<out Boolean>> = bot.makeRequestAsync(
//            TgMethod("createNewStickerSet"),
//            mapOf(
//                "user_id" to System.getenv("TELEGRAM_ID").toLong(),
//                "name" to "test1_by_venny_testbot",
//                "title" to "test1",
//                "sticker_format" to "static",
//                "stickers" to listOf(
//                    mapOf(
//                        "sticker" to image,
//                        "emoji_list" to listOf("\uD83D\uDE02"),
//                    ),
//                    mapOf(
//                        "sticker" to image,
//                        "emoji_list" to listOf("\uD83D\uDE02"),
//                    ),
//                    mapOf(
//                        "sticker" to image,
//                        "emoji_list" to listOf("\uD83D\uDE02"),
//                    ),
//                    mapOf(
//                        "sticker" to image,
//                        "emoji_list" to listOf("\uD83D\uDE02"),
//                    ),
//                ),
//            ),
//            Boolean::class.java,
//            (null as Class<MultipleResponse>?)
//        )

        val img =
            bot.httpClient.get(
                "https://img.freepik.com/free-vector/" +
                    "abstract-business-professional-background-banner-design-multipurpose_1340-16858.jpg" +
                    "?w=1800&t=st=1678553910~exp=1678554510~hmac=69bed2f1ba18372ae01cd30a1c6025830e33fd8177ed301c4970daa3820f9ce3",
            ).readBytes()

        @Suppress("UNCHECKED_CAST")
        val mediaGroupReq = bot.makeBunchMediaRequestAsync(
            TgMethod("createNewStickerSet"),
            mapOf(
                "test.png" to img,
                "test2.png" to img,
                "test3.png" to img,
                "test4.png" to img,
            ),
            parameters = mapOf(
                "user_id" to System.getenv("TELEGRAM_ID").toLong(),
                "name" to "test1_by_venny_testbot",
                "title" to "test1",
                "sticker_format" to "static",
                "stickers" to listOf(
                    mapOf(
                        "sticker" to "attach://test.png",
                        "emoji_list" to listOf("\uD83D\uDE02"),
                    ),
                    mapOf(
                        "sticker" to "attach://test2.png",
                        "emoji_list" to listOf("\uD83D\uDE02"),
                    ),
                    mapOf(
                        "sticker" to "attach://test3.png",
                        "emoji_list" to listOf("\uD83D\uDE02"),
                    ),
                    mapOf(
                        "sticker" to "attach://test4.png",
                        "emoji_list" to listOf("\uD83D\uDE02"),
                    ),
                ),
            ),
            ContentType.Image.PNG,
            List::class.java,
            Message::class.java,
        ).await().getOrNull() as? List<Message>
    }

    @Test
    suspend fun `media group requests testing via action`() {
        val image = classloader.getResource("image.png")?.toURI()
        image.shouldNotBeNull()

        val mediaRequest = mediaGroup(
            InputMedia.Photo(ImplicitFile.FromFile(File(image)), caption = "<b>test</b>", parseMode = ParseMode.HTML),
            InputMedia.Photo(ImplicitFile.FromFile(File(image))),
        ).sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await().getOrNull()

        mediaRequest.shouldNotBeNull()
        mediaRequest.first().mediaGroupId.shouldNotBeNull()
    }

    @Test
    suspend fun `passing mixed implicit types to media group `() {
        val image = classloader.getResource("image.png")?.toURI()
        image.shouldNotBeNull()

        val imageBytes = classloader.getResource("image.png")?.readBytes() ?: ByteArray(0)
        val mediaRequest = mediaGroup(
            InputMedia.Photo(ImplicitFile.FromFile(File(image)), caption = "<b>test</b>", parseMode = ParseMode.HTML),
            InputMedia.Photo(ImplicitFile.FromByteArray(imageBytes)),
            InputMedia.Photo(
                ImplicitFile.FromString(
                    "https://storage.myseldon.com/news-pict-fe/FEB5B36049C75ADDFF40C00221D2D9D5",
                ),
            ),
        ).sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await().getOrNull()

        mediaRequest.shouldNotBeNull()
        mediaRequest.first().mediaGroupId.shouldNotBeNull()
    }

    @Test
    fun `check mediaGroup action for different types passing`() {
        assertThrows<IllegalArgumentException>("All elements must be of the same specific type") {
            SendMediaGroupAction(
                InputMedia.Photo(ImplicitFile.FromString("")),
                InputMedia.Audio(ImplicitFile.FromString("")),
            )
        }
    }

    @Test
    fun `check mediaGroup action for unsupported types passing`() {
        assertThrows<IllegalArgumentException>("Only Audio/Document/Photo/Video is possible.") {
            SendMediaGroupAction(
                InputMedia.Animation(ImplicitFile.FromString("")),
                InputMedia.Photo(ImplicitFile.FromString("")),
            )
        }
    }
}
