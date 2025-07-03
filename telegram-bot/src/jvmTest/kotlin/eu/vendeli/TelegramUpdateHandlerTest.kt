package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.common.Update
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.Response
import eu.vendeli.tgbot.types.media.Document
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.common.onMessage
import eu.vendeli.tgbot.utils.common.processUpdate
import eu.vendeli.tgbot.utils.common.serde
import eu.vendeli.utils.MockUpdate
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.time.Instant
import kotlinx.serialization.builtins.ListSerializer

class TelegramUpdateHandlerTest : BotTestContext() {
    override fun isolationMode() = IsolationMode.InstancePerLeaf

    @ExperimentalKotest
    override fun concurrency(): Int = 1

    @Test
    suspend fun `listener workflow`() {
        doMockHttp(MockUpdate.RAW_RESPONSE(updates))

        var update: ProcessedUpdate? = null

        bot.update.setListener {
            update = it
            stopListener()
        }

        update.shouldNotBeNull()
        update.shouldBeTypeOf<MessageUpdate> {
            it.updateId shouldBe 53192527
            it.user.username shouldBe "username"
            it.user.firstName shouldBe "John Doe"
        }
    }

    @Test
    suspend fun `exception catching via functional handling`() {
        doMockHttp()

        bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
            .shouldBeNull()

        bot.handleUpdates {
            onMessage {
                throw NoSuchElementException("test")
            }
            delay(100)
            bot.update.stopListener()
        }
        val throwableUpdatePair = bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
        throwableUpdatePair.shouldNotBeNull()

        throwableUpdatePair.exception shouldNotBeSameInstanceAs NoSuchElementException::class
        throwableUpdatePair.exception.message shouldBe "test"

        throwableUpdatePair.update.origin.message
            .shouldNotBeNull()
        throwableUpdatePair.update.origin.message
            ?.text shouldBe "/start"
    }

    @Test
    suspend fun `exception catching via annotation handling`() {
        doMockHttp(MockUpdate.SINGLE("test"))

        bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
            .shouldBeNull()
        bot.update.setListener {
            handle(it)
            stopListener()
        }

        val throwableUpdatePair = bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
        throwableUpdatePair.shouldNotBeNull()

        throwableUpdatePair.exception shouldNotBeSameInstanceAs IllegalArgumentException::class
        throwableUpdatePair.exception.message shouldBe "test2"

        throwableUpdatePair.update.origin.message
            .shouldNotBeNull()
        throwableUpdatePair.update.origin.message
            ?.text shouldBe "test"
    }

    @Test
    suspend fun `commonhandler via annotation handling`() {
        doMockHttp(MockUpdate.SINGLE("common"))

        bot.update.setListener {
            handle(it)
            stopListener()
        }
        bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
            .shouldBeNull()
    }

    @Test
    suspend fun `deeplink test`() {
        doMockHttp(MockUpdate.SINGLE("/start test"))

        var commandReached = false
        bot.handleUpdates {
            onCommand("/start") {
                commandReached = true
                parameters shouldContainExactly mapOf("param_1" to "test")
            }
            bot.update.stopListener()
        }
        commandReached shouldBe true
    }

    @Test
    suspend fun `command over input priority test`() {
        doMockHttp(MockUpdate.TEXT_LIST("test", "aaaa"))
        bot.update.setListener {
            bot.inputListener.set(1, "testInp")
            handle(it)
            delay(200)
            if (it.text == "aaaa") stopListener()
        }

        bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
            .shouldNotBeNull()
    }

    @Test
    suspend fun `input media handling test`() {
        doMockHttp(
            MockUpdate.UPDATES_LIST(
                listOf(
                    Update(
                        1,
                        Message(
                            2,
                            from = DUMB_USER,
                            date = Instant.DISTANT_PAST,
                            chat = Chat(1, ChatType.Private),
                            document = Document("3", "33"),
                        ),
                    ).processUpdate(),
                ),
            ),
        )

        bot.update.setListener {
            bot.inputListener.set(1, "testInp")
            handle(it)
            stopListener()
        }
        bot.update.caughtExceptions.tryReceive().getOrNull().shouldNotBeNull().run {
            exception.message shouldBe "test3"
        }

        var inputReached = false
        bot.handleUpdates {
            bot.inputListener.set(1, "testInp")
            onInput("testInp") {
                inputReached = true
            }
            bot.update.stopListener()
        }
        inputReached.shouldBeTrue()
    }

    @Test
    suspend fun `webhook handling test`() {
        prepareTestBot()
        val rawUpdate = serde.run {
            encodeToString(
                decodeFromString(
                    Response.Success.serializer(ListSerializer(ProcessedUpdate.serializer())),
                    MockUpdate.SINGLE().response.toString(Charsets.UTF_8),
                ).result.first(),
            )
        }
        var update: ProcessedUpdate? = null
        bot.update.setBehaviour {
            update = it
        }
        shouldNotThrowAny {
            bot.update.parseAndHandle(rawUpdate)
            delay(1)
        }
        update.shouldNotBeNull()
    }

    @Test
    suspend fun `update flow test`() {
        val collectedUpdates = mutableListOf<ProcessedUpdate>()

        @Suppress("OPT_IN_USAGE")
        GlobalScope.launch {
            bot.update.flow
                .takeWhile {
                    it.text.startsWith("test")
                }.take(10)
                .toList(collectedUpdates)
        }
        doMockHttp(MockUpdate.TEXT_LIST("test1", "test2", "test3", "4test"))
        bot.handleUpdates { bot.update.stopListener() }

        delay(100)
        collectedUpdates.forEach { it.text shouldStartWith "test" }
    }

    companion object {
        val updates = """
            {"ok":true,"result":[{"update_id":53192527,
            "message":{"message_id":10831,"from":{"id":1,"is_bot":false,"first_name":"John Doe","username":"username","language_code":"en"},
            "chat":{"id":1,"first_name":"John Doe","username":"username","type":"private"},"date":1656908266,"text":"/start",
            "entities":[{"offset":0,"length":6,"type":"bot_command"}]}}]}
        """.trimIndent()
    }
}
