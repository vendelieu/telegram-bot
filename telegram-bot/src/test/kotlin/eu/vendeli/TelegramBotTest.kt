package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.getFile
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.interfaces.BotChatData
import eu.vendeli.tgbot.interfaces.BotUserData
import eu.vendeli.tgbot.interfaces.MagicObject
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.interfaces.sendAsync
import eu.vendeli.tgbot.types.File
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import eu.vendeli.tgbot.types.internal.onFailure
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeBlank
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

class TelegramBotTest : BotTestContext() {
    @Test
    fun `requests testing`() = runBlocking {
        val getMeReq = bot.makeRequestAsync(
            TgMethod("getMe"), null, User::class.java, (null as Class<MultipleResponse>?)
        ).await().getOrNull()

        getMeReq.shouldNotBeNull()
        getMeReq.isBot.shouldBeTrue()
    }

    @Test
    fun `test silent requesting`() = runBlocking {
        val silentReq = bot.makeSilentRequest(
            TgMethod("sendMessage"),
            mapOf("text" to "test", "chat_id" to System.getenv("TELEGRAM_ID").toLong())
        )

        silentReq.status shouldBe HttpStatusCode.OK
        silentReq.bodyAsText().shouldNotBeBlank()
        silentReq.bodyAsText() shouldContain "\"ok\":true"
        silentReq.bodyAsText() shouldContain "\"text\":\"test\""
    }

    @Test
    suspend fun `failure response handling`() {
        val failureReq = bot.makeRequestAsync(
            TgMethod("sendMessage"),
            mapOf("text" to "test"),
            Message::class.java,
            (null as Class<MultipleResponse>?)
        ).await()

        failureReq.isSuccess().shouldBeFalse()
        failureReq.getOrNull().shouldBeNull()

        failureReq.onFailure {
            it.ok.shouldBeFalse()
            it.errorCode shouldBe 400
            it.description shouldBe "Bad Request: chat_id is empty"
        }
    }

    @Test
    fun `user data setting`() {
        val bot = TelegramBot("") {
            context {
                userData = userDataImpl
            }
        }

        shouldNotThrow<NotImplementedError> { bot.userData }
        bot.userData shouldBeEqualToComparingFields userDataImpl
    }

    @Test
    fun `chat data not set`() {
        shouldThrow<NotImplementedError> {
            bot.chatData
        }
    }

    @Test
    fun `chat data setting`() {
        val bot = TelegramBot("") {
            context {
                chatData = chatDataImpl
            }
        }

        shouldNotThrow<NotImplementedError> { bot.chatData }
        bot.chatData shouldBeEqualToComparingFields chatDataImpl
    }

    @Test
    fun `adding magic object`() {
        bot.addMagicObject(TgMethod::class.java) {
            object : MagicObject<TgMethod> {
                override fun get(update: ProcessedUpdate, bot: TelegramBot): TgMethod =
                    TgMethod("test")
            }
        }

        bot.magicObjects[TgMethod::class.java].shouldNotBeNull()

        bot.magicObjects[TgMethod::class.java]?.get(dummyProcessedUpdate, bot)
            ?.shouldBeEqualToComparingFields(TgMethod("test"))
    }

    @Test
    fun `media request handling`() = runBlocking {
        val image = classloader.getResource("image.png")?.readBytes()
        image.shouldNotBeNull()

        val mediaReq = bot.makeSilentRequest(
            TgMethod("sendPhoto"),
            "photo",
            "image.jpg",
            image,
            mapOf("chat_id" to System.getenv("TELEGRAM_ID").toLong()),
            ContentType.Image.JPEG
        )

        mediaReq.status shouldBe HttpStatusCode.OK
        mediaReq.bodyAsText().isNotBlank().shouldBeTrue()
        mediaReq.bodyAsText() shouldContain "\"ok\":true"
        mediaReq.bodyAsText() shouldContain "\"file_id\""
        bot.makeRequestAsync(
            TgMethod("sendPhoto"),
            "photo",
            "image.jpg",
            image,
            mapOf("chat_id" to System.getenv("TELEGRAM_ID").toLong()),
            ContentType.Image.JPEG,
            Message::class.java,
            (null as Class<MultipleResponse>?)
        ).await().isSuccess().shouldBeTrue()
    }

    @Test
    fun `getting file url`() = runBlocking {
        val image = classloader.getResource("image.png")?.readBytes()
        image.shouldNotBeNull()

        val fileId = photo(image).sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await()
            .getOrNull()?.photo?.first()?.fileId ?: throw NullPointerException()

        val file = getFile(fileId).sendAsync(bot).await().getOrNull()

        file.shouldNotBeNull()
        val fileUrl = bot.getFileDirectUrl(file)
        fileUrl.shouldNotBeNull()

        val fileBA = bot.getFileContent(file)
        fileBA.shouldNotBeNull()
    }

    @Test
    fun `try to get file without filePath`() = runBlocking {
        val file = File("", "")
        bot.getFileDirectUrl(file).shouldBeNull()
        bot.getFileContent(file).shouldBeNull()
    }

    companion object {
        val dummyProcessedUpdate = ProcessedUpdate(
            UpdateType.MESSAGE,
            null,
            User.EMPTY,
            Update(-1)
        )

        val userDataImpl = object : BotUserData {
            override fun set(telegramId: Long, key: String, value: Any?) = TODO("Not yet implemented")
            override suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean> =
                TODO("Not yet implemented")

            override fun <T> get(telegramId: Long, key: String): T = TODO("Not yet implemented")
            override suspend fun <T> getAsync(telegramId: Long, key: String): Deferred<T?> =
                TODO("Not yet implemented")

            override fun del(telegramId: Long, key: String) = TODO("Not yet implemented")
            override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> =
                TODO("Not yet implemented")
        }

        val chatDataImpl = object : BotChatData {
            override fun set(telegramId: Long, key: String, value: Any?) = TODO("Not yet implemented")
            override suspend fun setAsync(telegramId: Long, key: String, value: Any?): Deferred<Boolean> =
                TODO("Not yet implemented")

            override fun <T> get(telegramId: Long, key: String): T =
                TODO("Not yet implemented")

            override suspend fun <T> getAsync(telegramId: Long, key: String): Deferred<T?> =
                TODO("Not yet implemented")

            override fun del(telegramId: Long, key: String) = TODO("Not yet implemented")
            override suspend fun delAsync(telegramId: Long, key: String): Deferred<Boolean> =
                TODO("Not yet implemented")

            override fun delPrevChatSection(telegramId: Long) = TODO("Not yet implemented")
            override suspend fun delPrevChatSectionAsync(telegramId: Long): Deferred<Boolean> =
                TODO("Not yet implemented")
        }
    }
}
