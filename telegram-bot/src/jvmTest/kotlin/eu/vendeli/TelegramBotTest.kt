package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.botactions.getMe
import eu.vendeli.tgbot.api.getFile
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.implementations.EnvConfigLoaderImpl
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.LogLvl
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.foldResponse
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import eu.vendeli.tgbot.types.internal.onFailure
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import eu.vendeli.tgbot.utils.toJsonElement
import eu.vendeli.tgbot.utils.toPartData
import eu.vendeli.utils.MockUpdate
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
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.JsonUnquotedLiteral
import kotlinx.serialization.serializer
import other.pckg.ChatDataImpl
import other.pckg.UserDataImpl

class TelegramBotTest : BotTestContext() {
    @Test
    suspend fun `updates handler shortcut test`() {
        doMockHttp(MockUpdate.SINGLE("STOP"))
        shouldNotThrowAny {
            bot.handleUpdates()
        }
    }

    @OptIn(InternalSerializationApi::class)
    @Test
    suspend fun `requests testing`() {
        val getMeReq = bot.makeRequestAsync(
            TgMethod("getMe"),
            emptyMap(),
            User::class.serializer(),
            emptyList(),
        ).await().getOrNull()

        getMeReq.shouldNotBeNull()
        getMeReq.isBot.shouldBeTrue()
    }

    @Test
    suspend fun `test silent requesting`() {
        val silentReq = bot.makeSilentRequest(
            TgMethod("sendMessage"),
            mapOf("text" to "test".toJsonElement(), "chat_id" to TG_ID.toJsonElement()),
            emptyList(),
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
            mapOf("text" to "test".toJsonElement()),
            Message.serializer(),
            emptyList(),
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
    suspend fun `fold response handling`() {
        val req = getMe().sendAsync(bot)

        var isFailure: Boolean? = null
        req.foldResponse(
            {
                isFailure = false
                result.isBot
            },
            {
                isFailure = true
            },
        ) shouldBe true

        isFailure shouldBe false
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

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    @Test
    suspend fun `media request handling`() {
        val image = classloader.getResource("image.png")?.readBytes()
        image.shouldNotBeNull()

        val mediaReq = bot.makeSilentRequest(
            method = TgMethod("sendPhoto"),
            data = mapOf(
                "photo" to JsonUnquotedLiteral("attach://image.jpg"),
                "chat_id" to TG_ID.toJsonElement(),
            ),
            listOf(InputFile(image, "image.jpg", ContentType.Image.JPEG.contentType).toPartData("image.jpg")),
        )
        mediaReq.status shouldBe HttpStatusCode.OK
        mediaReq.bodyAsText().isNotBlank().shouldBeTrue()
        mediaReq.bodyAsText() shouldContain "\"ok\":true"
        mediaReq.bodyAsText() shouldContain "\"file_id\""

        bot.makeRequestAsync(
            method = TgMethod("sendPhoto"),
            mapOf(
                "photo" to JsonUnquotedLiteral("attach://image.jpg"),
                "chat_id" to TG_ID.toJsonElement(),
            ),
            Message::class.serializer(),
            listOf(InputFile(image, "image.jpg", ContentType.Image.JPEG.contentType).toPartData("image.jpg")),
        ).await().isSuccess().shouldBeTrue()
    }

    @Test
    suspend fun `getting file url`() {
        val image = classloader.getResource("image.png")?.readBytes()
        image.shouldNotBeNull()

        val fileId = photo(image).sendReturning(TG_ID, bot)
            .getOrNull()?.photo?.first()?.fileId!!

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
        shouldThrow<NullPointerException> { TelegramBot(EnvConfigLoaderImpl) }

        EnvConfigLoaderImpl.envVars = mapOf(
            "TGBOT_TOKEN" to "test",
            "TGBOT_COMMANDS_PACKAGE" to "com.example",
            "TGBOT_API_HOST" to "tg.com",
            "TGBOT_INPUT_LISTENER" to "eu.vendeli.tgbot.implementations.InputListenerMapImpl",
            "TGBOT_CLASS_MANAGER" to "eu.vendeli.tgbot.implementations.ClassManagerImpl",
            "TGBOT_RATE_LIMITER" to "eu.vendeli.tgbot.implementations.TokenBucketLimiterImpl",
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
        shouldNotThrowAny { TelegramBot(EnvConfigLoaderImpl) }.config.apply {
            apiHost shouldBe "tg.com"
            inputListener::class.java shouldBe
                Class.forName("eu.vendeli.tgbot.implementations.InputListenerMapImpl")
            classManager::class.java shouldBe
                Class.forName("eu.vendeli.tgbot.implementations.ClassManagerImpl")
            rateLimiter.mechanism::class.java shouldBe
                Class.forName("eu.vendeli.tgbot.implementations.TokenBucketLimiterImpl")

            httpClient.requestTimeoutMillis shouldBe 10
            httpClient.connectTimeoutMillis shouldBe 11
            httpClient.socketTimeoutMillis shouldBe 12
            httpClient.maxRequestRetry shouldBe 13
            httpClient.retryDelay shouldBe 1000

            botLogLevel shouldBe LogLvl.WARN

            rateLimiter.limits.period shouldBe 14
            rateLimiter.limits.rate shouldBe 15

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
            Message(
                -0,
                chat = Chat(
                    -0,
                    type = ChatType.Private,
                ),
                date = Instant.DISTANT_PAST,
                from = User(1, false, "Test"),
            ),
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
