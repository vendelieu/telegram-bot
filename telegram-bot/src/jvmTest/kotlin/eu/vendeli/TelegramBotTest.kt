package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.botactions.getMe
import eu.vendeli.tgbot.api.media.getFile
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.api.message.editMessageText
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.interfaces.ctx.InputListener
import eu.vendeli.tgbot.types.common.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.component.ExceptionHandlingStrategy
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.component.foldResponse
import eu.vendeli.tgbot.types.component.getOrNull
import eu.vendeli.tgbot.types.component.isSuccess
import eu.vendeli.tgbot.types.component.onFailure
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.common.TgException
import eu.vendeli.tgbot.utils.common.TgFailureException
import eu.vendeli.tgbot.utils.internal.makeRequestReturning
import eu.vendeli.tgbot.utils.internal.makeSilentRequest
import eu.vendeli.tgbot.utils.internal.toJsonElement
import eu.vendeli.utils.MockUpdate
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlinx.coroutines.Deferred
import kotlin.time.Instant
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer

class TelegramBotTest : BotTestContext() {
    override fun threads() = 1

    @ExperimentalKotest
    override fun concurrency() = 1
    override fun isolationMode() = IsolationMode.InstancePerLeaf

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
        val getMeReq = bot
            .makeRequestReturning(
                "getMe",
                emptyMap(),
                User::class.serializer(),
                emptyList(),
            ).await()
            .getOrNull()

        getMeReq.shouldNotBeNull()
        getMeReq.isBot.shouldBeTrue()
    }

    @Test
    suspend fun `test silent requesting`() {
        shouldNotThrowAny {
            bot.makeSilentRequest(
                "sendMessage",
                mapOf("text" to "test".toJsonElement(), "chat_id" to TG_ID.toJsonElement()),
                emptyList(),
            )
        }
    }

    @Test
    suspend fun `failure response handling`() {
        val failureReq = bot
            .makeRequestReturning(
                "sendMessage",
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
        val req = getMe().sendReturning(bot)

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
            inputListener = inputListenerImpl
        }

        bot.inputListener shouldBeEqualToComparingFields inputListenerImpl
    }

    @Test
    suspend fun `getting file url`() {
        val image = classloader.getResource("image.png")?.readBytes()
        image.shouldNotBeNull()

        val fileId = photo(image)
            .sendReq()
            .getOrNull()
            ?.photo
            ?.first()
            ?.fileId!!

        val file = getFile(fileId).sendReq().shouldSuccess()

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
    suspend fun `check exception catch turned off`() {
        bot.config.exceptionHandlingStrategy = ExceptionHandlingStrategy.Throw
        doMockHttp(MockUpdate.SINGLE("test"))
        bot.update.setListener {
            shouldThrow<TgException> {
                handle(it)
            }
            stopListener()
        }
        bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
            .shouldBeNull()

        bot.config.exceptionHandlingStrategy = ExceptionHandlingStrategy.CollectToChannel
        doMockHttp(MockUpdate.SINGLE("test"))
        bot.update.setListener {
            shouldNotThrowAny {
                handle(it)
            }
            stopListener()
        }
        bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
            .shouldNotBeNull()
    }

    @Test
    suspend fun `check failure throws exception`() {
        bot.config.throwExOnActionsFailure = false
        shouldNotThrowAny {
            editMessageText { "test" }.sendInline("test", bot)
        }

        bot.config.throwExOnActionsFailure = true
        shouldThrow<TgFailureException> {
            editMessageText { "test" }.sendInline("test", bot)
        }.message shouldContain "\"error_code\":400"
    }

    @Test
    suspend fun `check methodName correctness`() {
        val action = message { "" }
        action.apply {
            methodName shouldBe "sendMessage"
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
