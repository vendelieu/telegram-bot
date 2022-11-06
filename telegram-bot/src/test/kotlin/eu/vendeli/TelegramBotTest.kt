package eu.vendeli

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.getFile
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.interfaces.*
import eu.vendeli.tgbot.types.File
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TelegramBotTest {
    private lateinit var bot: TelegramBot
    private var classloader = Thread.currentThread().contextClassLoader

    @BeforeAll
    fun prepareTestBot() {
        bot = TelegramBot.Builder(System.getenv("BOT_TOKEN")).build()
    }

    @Test
    fun `requests testing`() = runBlocking {
        val getMeReq = bot.makeRequestAsync(
            TgMethod("getMe"), null, User::class.java, (null as Class<MultipleResponse>?)
        ).await().getOrNull()

        assertNotNull(getMeReq)
        assertEquals("testbot", getMeReq?.firstName)
        assertTrue(getMeReq?.isBot ?: false)
    }

    @Test
    fun `test silent requesting`() = runBlocking {
        val silentReq = bot.makeSilentRequest(
            TgMethod("sendMessage"),
            mapOf("text" to "test", "chat_id" to System.getenv("TELEGRAM_ID"))
        )!!

        assertEquals(silentReq.status, HttpStatusCode.OK)
        assertTrue(silentReq.bodyAsText().isNotBlank())
        assertTrue(silentReq.bodyAsText().contains("\"ok\":true"))
        assertTrue(silentReq.bodyAsText().contains("\"text\":\"test\""))
    }

    @Test
    fun `failure response handling`(): Unit = runBlocking {
        val failureReq = bot.makeRequestAsync(
            TgMethod("sendMessage"),
            mapOf("text" to "test"),
            Message::class.java,
            (null as Class<MultipleResponse>?)
        ).await()

        assertFalse(failureReq.isSuccess())
        assertNull(failureReq.getOrNull())

        failureReq.onFailure {
            assertFalse(it.ok)
            assertEquals(400, it.errorCode)
            assertEquals("Bad Request: chat_id is empty", it.description)
        }
    }

    @Test
    fun `user data setting`() {
        assertNull(bot.userData)
        bot.userData = userDataImpl
        assertNotNull(bot.userData)
        assertEquals(userDataImpl, bot.userData)
    }

    @Test
    fun `chat data setting`() {
        assertNull(bot.chatData)
        bot.chatData = chatDataImpl
        assertNotNull(bot.chatData)
        assertEquals(chatDataImpl, bot.chatData)
    }

    @Test
    fun `adding magic object`() {
        bot.addMagicObject(TgMethod::class.java) {
            object : MagicObject<TgMethod> {
                override fun get(update: ProcessedUpdate, bot: TelegramBot): TgMethod? {
                    return TgMethod("test")
                }
            }
        }

        assertNotNull(bot.magicObjects[TgMethod::class.java])

        assertEquals(
            TgMethod("test"),
            bot.magicObjects[TgMethod::class.java]?.get(
                ProcessedUpdate(
                    UpdateType.MESSAGE,
                    null,
                    User.EMPTY,
                    Update(-1)
                ),
                bot
            )
        )
    }

    @Test
    fun `media request handling`() = runBlocking {
        val image = classloader.getResource("image.png")?.readBytes() ?: ByteArray(0)

        val mediaReq = bot.makeSilentRequest(
            TgMethod("sendPhoto"),
            "photo",
            "image.jpg",
            image,
            mapOf("chat_id" to System.getenv("TELEGRAM_ID")),
            ContentType.Image.JPEG
        )!!

        assertEquals(mediaReq.status, HttpStatusCode.OK)
        assertTrue(mediaReq.bodyAsText().isNotBlank())
        assertTrue(mediaReq.bodyAsText().contains("\"ok\":true"))
        assertTrue(mediaReq.bodyAsText().contains("\"file_id\""))

        assertTrue(
            bot.makeRequestAsync(
                TgMethod("sendPhoto"),
                "photo",
                "image.jpg",
                image,
                mapOf("chat_id" to System.getenv("TELEGRAM_ID")),
                ContentType.Image.JPEG,
                Message::class.java,
                (null as Class<MultipleResponse>?)
            ).await().isSuccess()
        )
    }

    @Test
    fun `getting file url`() = runBlocking {
        val image = classloader.getResource("image.png")?.readBytes() ?: ByteArray(0)
        val fileId = photo(image).sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await()
            .getOrNull()?.photo?.first()?.fileId ?: throw NullPointerException()

        val file = getFile(fileId).sendAsync(bot).await().getOrNull()

        assertNotNull(file)

        val fileUrl = bot.getFileDirectUrl(file!!)
        assertNotNull(fileUrl)

        val fileBA = bot.getFileContent(file)
        assertNotNull(fileBA)
    }

    @Test
    fun `try to get file without filePath`() = runBlocking {
        val file = File("", "")
        assertNull(bot.getFileDirectUrl(file))
        assertNull(bot.getFileContent(file))
    }

    companion object {
        val userDataImpl = object : BotUserData {
            override fun set(telegramId: Long, key: String, value: Any?) {
                TODO("Not yet implemented")
            }

            override suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean> {
                TODO("Not yet implemented")
            }

            override fun get(telegramId: Long, key: String): Any? {
                TODO("Not yet implemented")
            }

            override suspend fun getAsync(telegramId: Long, key: String): Deferred<Any?> {
                TODO("Not yet implemented")
            }

            override fun del(telegramId: Long, key: String) {
                TODO("Not yet implemented")
            }

            override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> {
                TODO("Not yet implemented")
            }
        }

        val chatDataImpl = object : BotChatData {
            override fun set(telegramId: Long, key: String, value: Any?) {
                TODO("Not yet implemented")
            }

            override suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean> {
                TODO("Not yet implemented")
            }

            override fun get(telegramId: Long, key: String): Any? {
                TODO("Not yet implemented")
            }

            override suspend fun getAsync(telegramId: Long, key: String): Deferred<Any?> {
                TODO("Not yet implemented")
            }

            override fun del(telegramId: Long, key: String) {
                TODO("Not yet implemented")
            }

            override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> {
                TODO("Not yet implemented")
            }

            override fun delPrevChatSection(telegramId: Long) {
                TODO("Not yet implemented")
            }

            override suspend fun delPrevChatSectionAsync(telegramId: Long): Deferred<Boolean> {
                TODO("Not yet implemented")
            }

        }
    }
}
