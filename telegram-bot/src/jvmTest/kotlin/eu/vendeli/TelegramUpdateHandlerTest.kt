package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.media.Document
import eu.vendeli.tgbot.utils.parseCommand
import eu.vendeli.tgbot.utils.processUpdate
import eu.vendeli.tgbot.utils.serde
import eu.vendeli.utils.MockUpdate
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlinx.coroutines.delay
import kotlinx.datetime.Instant
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString

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
    fun `valid command parsing`() {
        bot.config.apply {
            commandParsing.restrictSpacesInCommands = true
        }
        val deeplinkParse = bot.update.parseCommand("/start deeplinkcode")
        deeplinkParse.command shouldBe "/start"
        deeplinkParse.params shouldContainExactly (mapOf("param_1" to "deeplinkcode"))

        val commandParseWithNoParams = bot.update.parseCommand("/command")
        commandParseWithNoParams.command shouldBe "/command"
        commandParseWithNoParams.params.size shouldBe 0

        val commandParseWithOneEmptyParam = bot.update.parseCommand("/command?")
        commandParseWithOneEmptyParam.command shouldBe "/command"
        commandParseWithOneEmptyParam.params shouldContainExactly (mapOf("param_1" to ""))

        val commandParseWithMixedParams = bot.update.parseCommand("command?p1=v1&v2&p3=&p4=v4&p5=")
        commandParseWithMixedParams.command shouldBe "command"
        commandParseWithMixedParams.params shouldContainExactly mapOf(
            "p1" to "v1",
            "param_2" to "v2",
            "p3" to "",
            "p4" to "v4",
            "p5" to "",
        )

        val commandParseForLastFullPair = bot.update.parseCommand("last_pair_command?v1&p2=v2")
        commandParseForLastFullPair.command shouldBe "last_pair_command"
        commandParseForLastFullPair.params shouldContainExactly (mapOf("param_1" to "v1", "p2" to "v2"))

        bot.config.commandParsing.apply {
            commandDelimiter = '_'
        }

        val underscoreCommand = bot.update.parseCommand("/test_123")
        underscoreCommand.command shouldBe "/test"
        underscoreCommand.params shouldContainExactly (mapOf("param_1" to "123"))

        bot.config.commandParsing.apply {
            commandDelimiter = ' '
            parametersDelimiter = ' '
            parameterValueDelimiter = ' '
            restrictSpacesInCommands = false
        }
        val deeplinkCheck = bot.update.parseCommand("/start bafefdf0-64cb-47da-97f0-4a1f11d469a2")
        deeplinkCheck.command shouldBe "/start"
        deeplinkCheck.params shouldContainExactly (mapOf("param_1" to "bafefdf0-64cb-47da-97f0-4a1f11d469a2"))
    }

    @Test
    suspend fun `deeplink test`() {
        doMockHttp(MockUpdate.SINGLE("/start test"))

        bot.handleUpdates {
            onCommand("/start") {
                parameters shouldContainExactly mapOf("deepLink" to "test")
            }
            bot.update.stopListener()
        }
    }

    @Test
    suspend fun `command over input priority test`() {
        doMockHttp(MockUpdate.TEXT_LIST(listOf("test", "aaaa")))
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

    @OptIn(InternalApi::class)
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

    companion object {
        val updates = """
            {"ok":true,"result":[{"update_id":53192527,
            "message":{"message_id":10831,"from":{"id":1,"is_bot":false,"first_name":"John Doe","username":"username","language_code":"en"},
            "chat":{"id":1,"first_name":"John Doe","username":"username","type":"private"},"date":1656908266,"text":"/start",
            "entities":[{"offset":0,"length":6,"type":"bot_command"}]}}]}
        """.trimIndent()
    }
}
