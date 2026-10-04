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
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import kotlin.time.Instant
import kotlinx.serialization.builtins.ListSerializer

private const val AWAIT_TIMEOUT_MS = 5_000L

class TelegramUpdateHandlerTest : BotTestContext() {
    override fun isolationMode() = IsolationMode.InstancePerLeaf

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

        bot.setFunctionality {
            onMessage {
                throw NoSuchElementException("test")
            }
        }
        bot.update.setListener {
            handle(it)
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

        val commandReached = AtomicBoolean(false)
        bot.setFunctionality {
            onCommand("/start") {
                commandReached.set(true)
                parameters shouldContainExactly mapOf("param_1" to "test")
            }
        }
        bot.update.setListener {
            handle(it)
            bot.update.stopListener()
        }
        commandReached.get() shouldBe true
    }

    @Test
    suspend fun `command over input priority test`() {
        doMockHttp(MockUpdate.TEXT_LIST("test", "aaaa"))
        bot.update.setListener {
            bot.inputListener.set(1, "testInp")
            handle(it)
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

        val inputReached = AtomicBoolean(false)
        bot.setFunctionality {
            onInput("testInp") {
                inputReached.set(true)
            }
        }
        bot.update.setListener {
            bot.inputListener.set(1, "testInp")
            handle(it)
            bot.update.stopListener()
        }
        inputReached.get().shouldBeTrue()
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
        val received = CompletableDeferred<ProcessedUpdate>()
        bot.update.setBehaviour {
            received.complete(it)
        }
        shouldNotThrowAny {
            withTimeout(AWAIT_TIMEOUT_MS) { bot.update.parseAndHandle(rawUpdate).join() }
        }
        received.isCompleted.shouldBeTrue()
    }

    @Test
    suspend fun `update flow test`() = coroutineScope {
        val firstUpdate = async(start = CoroutineStart.UNDISPATCHED) { bot.update.flow.first() }
        doMockHttp(MockUpdate.TEXT_LIST("test1", "test2", "test3", "4test"))
        bot.update.setListener {
            handle(it)
            bot.update.stopListener()
        }

        withTimeout(AWAIT_TIMEOUT_MS) { firstUpdate.await() }.text shouldStartWith "test"
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
