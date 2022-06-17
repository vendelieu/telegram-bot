import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.getFile
import eu.vendeli.tgbot.interfaces.MagicObject
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.interfaces.sendAsync
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.*
import io.ktor.client.statement.*
import io.ktor.http.*
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
        bot = TelegramBot(System.getenv("BOT_TOKEN"), "eu.vendeli")
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
        var silentReq = bot.makeSilentRequest(
            TgMethod("sendMessage"),
            mapOf("text" to "test", "chat_id" to System.getenv("TELEGRAM_ID"))
        )

        assertEquals(silentReq.status, HttpStatusCode.OK)
        assertTrue(silentReq.bodyAsText().isNotBlank())
        assertTrue(silentReq.bodyAsText().contains("\"ok\":true"))
        assertTrue(silentReq.bodyAsText().contains("\"text\":\"test\""))


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
                    User(-0, false, ""),
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
        )

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
                mapOf("chat_id" to "519279767"),
                ContentType.Image.JPEG,
                Message::class.java,
                (null as Class<MultipleResponse>?)
            ).await().isSuccess()
        )
    }

    @Test
    fun `getting file url`() = runBlocking {
        val file = getFile("AgACAgIAAxkDAAMXYqzlFz9qLAOjLNB9heo0yzcffbkAAkW9MRuMEWlJQw2ZmuUMkRABAAMCAANtAAMkBA")
            .sendAsync(bot).await().getOrNull()

        assertNotNull(file)

        val fileUrl = bot.getFileDirectUrl(file!!)
        assertNotNull(fileUrl)

        val fileBA = bot.getFileContent(file)
        assertNotNull(fileBA)
    }
}