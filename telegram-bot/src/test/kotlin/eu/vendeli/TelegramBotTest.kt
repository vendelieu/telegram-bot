package eu.vendeli

import BotTestContext
import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.getFile
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.core.EnvConfigLoader
import eu.vendeli.tgbot.interfaces.Autowiring
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.HttpLogLevel
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import eu.vendeli.tgbot.types.internal.onFailure
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Deferred
import other.pckg.ChatDataImpl
import other.pckg.UserDataImpl

class TelegramBotTest : BotTestContext() {
    @Test
    suspend fun `requests testing`() {
        val getMeReq = bot.makeRequestAsync(
            TgMethod("getMe"),
            null,
            User::class.java,
            (null as Class<MultipleResponse>?),
        ).await().getOrNull()

        getMeReq.shouldNotBeNull()
        getMeReq.isBot.shouldBeTrue()
    }

    @Test
    suspend fun `test silent requesting`() {
        val silentReq = bot.makeSilentRequest(
            TgMethod("sendMessage"),
            mapOf("text" to "test", "chat_id" to TG_ID),
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
            (null as Class<MultipleResponse>?),
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
    fun `input listener setting`() {
        bot.inputListener shouldNotBeSameInstanceAs inputListenerImpl // check default impl
        val bot = TelegramBot("") {
            context {
                inputListener = inputListenerImpl
            }
        }

        bot.inputListener shouldBeEqualToComparingFields inputListenerImpl
    }

    @Test
    fun `user data setting`() {
        val bot = TelegramBot("") {
            context {
                userData = UserDataImpl()
            }
        }

        shouldNotThrow<NotImplementedError> { bot.userData }
        bot.userData::class.java shouldBe UserDataImpl::class.java
    }

    @Test
    fun `chat data setting`() {
        val bot = TelegramBot("") {
            context {
                chatData = ChatDataImpl()
            }
        }

        bot.chatData::class.java shouldBe ChatDataImpl::class.java
    }

    @Test
    suspend fun `adding magic object`() {
        bot.addAutowiringObject(TgMethod::class.java) {
            object : Autowiring<TgMethod> {
                override suspend fun get(update: ProcessedUpdate, bot: TelegramBot): TgMethod =
                    TgMethod("test")
            }
        }

        bot.autowiringObjects[TgMethod::class.java].shouldNotBeNull()

        bot.autowiringObjects[TgMethod::class.java]?.get(dummyProcessedUpdate, bot)
            ?.shouldBeEqualToComparingFields(TgMethod("test"))
    }

    @Test
    suspend fun `media request handling`() {
        val image = classloader.getResource("image.png")?.readBytes()
        image.shouldNotBeNull()

        val mediaReq = bot.makeSilentRequest {
            method = TgMethod("sendPhoto")
            dataField = "photo"
            name = "image.jpg"
            data = image
            parameters = mapOf("chat_id" to TG_ID)
            contentType = ContentType.Image.JPEG
        }

        mediaReq.status shouldBe HttpStatusCode.OK
        mediaReq.bodyAsText().isNotBlank().shouldBeTrue()
        mediaReq.bodyAsText() shouldContain "\"ok\":true"
        mediaReq.bodyAsText() shouldContain "\"file_id\""
        bot.makeRequestAsync(
            Message::class.java,
            (null as Class<MultipleResponse>?),
        ) {
            method = TgMethod("sendPhoto")
            dataField = "photo"
            name = "image.jpg"
            data = image
            parameters = mapOf("chat_id" to TG_ID)
            contentType = ContentType.Image.JPEG
        }.await().isSuccess().shouldBeTrue()
    }

    @Test
    suspend fun `getting file url`() {
        val image = classloader.getResource("image.png")?.readBytes()
        image.shouldNotBeNull()

        val fileId = photo(image).sendReturning(TG_ID, bot)
            .getOrNull()?.photo?.first()?.fileId ?: throw NullPointerException()

        val file = getFile(fileId).sendAsync(bot).await().getOrNull()

        file.shouldNotBeNull()
        val fileUrl = bot.getFileDirectUrl(file)
        fileUrl.shouldNotBeNull()

        val fileBA = bot.getFileContent(file)
        fileBA.shouldNotBeNull()
    }

    @Test
    suspend fun `try to get file without filePath`() {
        val file = File("", "")
        bot.getFileDirectUrl(file).shouldBeNull()
        bot.getFileContent(file).shouldBeNull()
    }

    @Test
    fun `env configure testing`() {
        // when token not specified np will be thrown
        shouldThrow<NullPointerException> { TelegramBot() }

        EnvConfigLoader.envVars = mapOf(
            "TGBOT_TOKEN" to "test",
            "TGBOT_COMMANDS_PACKAGE" to "com.example",
            "TGBOT_API_HOST" to "tg.com",
            "TGBOT_INPUT_LISTENER" to "eu.vendeli.tgbot.core.InputListenerMapImpl",
            "TGBOT_CLASS_MANAGER" to "eu.vendeli.tgbot.core.ClassManagerImpl",
            "TGBOT_RATE_LIMITER" to "eu.vendeli.tgbot.core.TokenBucketLimiterImpl",
            "TGBOT_HTTPC_RQ_TIMEOUT_MILLIS" to "10",
            "TGBOT_HTTPC_C_TIMEOUT_MILLIS" to "11",
            "TGBOT_HTTPC_SOC_TIMEOUT_MILLIS" to "12",
            "TGBOT_HTTPC_MAX_RQ_RETRY" to "13",
            "TGBOT_HTTPC_RETRY_DELAY" to "1000",
            "TGBOT_LOG_BOT_LVL" to "WARN",
            "TGBOT_LOG_HTTP_LVL" to "ALL",
            "TGBOT_RATES_PERIOD" to "14",
            "TGBOT_RATES_RATE" to "15",
            "TGBOT_CTX_USER_DATA" to "other.pckg.UserDataImpl",
            "TGBOT_CTX_CHAT_DATA" to "other.pckg.ChatDataImpl",
            "TGBOT_CMDPRS_CMD_DELIMITER" to " ",
            "TGBOT_CMDPRS_PARAMS_DELIMITER" to "-",
            "TGBOT_CMDPRS_PARAMVAL_DELIMITER" to "+",
            "TGBOT_CMDPRS_RESTRICT_SPC_INCMD" to "false",
        )
        shouldNotThrowAny { TelegramBot() }.config.apply {
            apiHost shouldBe "tg.com"
            inputListener::class.java shouldBe Class.forName("eu.vendeli.tgbot.core.InputListenerMapImpl")
            classManager::class.java shouldBe Class.forName("eu.vendeli.tgbot.core.ClassManagerImpl")
            rateLimiter::class.java shouldBe Class.forName("eu.vendeli.tgbot.core.TokenBucketLimiterImpl")

            httpClient.requestTimeoutMillis shouldBe 10
            httpClient.connectTimeoutMillis shouldBe 11
            httpClient.socketTimeoutMillis shouldBe 12
            httpClient.maxRequestRetry shouldBe 13
            httpClient.retryDelay shouldBe 1000

            logging.botLogLevel shouldBe Level.WARN
            logging.httpLogLevel shouldBe HttpLogLevel.ALL

            rateLimits.period shouldBe 14
            rateLimits.rate shouldBe 15

            context.userData::class.java shouldBe Class.forName("other.pckg.UserDataImpl")
            context.chatData::class.java shouldBe Class.forName("other.pckg.ChatDataImpl")

            commandParsing.commandDelimiter shouldBe ' '
            commandParsing.parametersDelimiter shouldBe '-'
            commandParsing.parameterValueDelimiter shouldBe '+'
            commandParsing.restrictSpacesInCommands shouldBe false
        }
    }

    internal companion object {
        val dummyProcessedUpdate = MessageUpdate(
            -0,
            Update(-1),
            Message(-0, chat = Chat(-0, type = ChatType.Private), date = -0),
        )

        val inputListenerImpl = object : InputListener {
            override fun set(telegramId: Long, identifier: String) = TODO("Not yet implemented")
            override suspend fun setAsync(telegramId: Long, identifier: String): Deferred<Boolean> =
                TODO("Not yet implemented")

            override fun get(telegramId: Long): String = TODO("Not yet implemented")
            override suspend fun getAsync(telegramId: Long): Deferred<String?> = TODO("Not yet implemented")
            override fun del(telegramId: Long) = TODO("Not yet implemented")
            override suspend fun delAsync(telegramId: Long): Deferred<Boolean> = TODO("Not yet implemented")
        }
    }
}
