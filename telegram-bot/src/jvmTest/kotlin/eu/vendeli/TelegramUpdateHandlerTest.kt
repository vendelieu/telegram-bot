package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.configuration.CommandParsingConfiguration
import eu.vendeli.tgbot.types.media.Document
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.getParameters
import eu.vendeli.tgbot.utils.onMessage
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
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
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
        val commandParseWithNoParams = bot.update.parseCommand("/command")
        val commandParseWithNoParamsParams = bot.update.getParameters(DefaultArgParser::class, commandParseWithNoParams)
        commandParseWithNoParams.command shouldBe "/command"
        commandParseWithNoParamsParams.size shouldBe 0

        val commandParseWithOneEmptyParam = bot.update.parseCommand("/command? ")
        val commandParseWithOneEmptyParamParams = bot.update.getParameters(
            DefaultArgParser::class,
            commandParseWithOneEmptyParam,
        )
        commandParseWithOneEmptyParam.command shouldBe "/command"
        commandParseWithOneEmptyParamParams shouldContainExactly mapOf("param_1" to " ")

        val commandParseWithMixedParams = bot.update.parseCommand("command?p1=v1&v2&p3=&p4=v4&p5=")
        val commandParseWithMixedParamsParams = bot.update.getParameters(
            DefaultArgParser::class,
            commandParseWithMixedParams,
        )
        commandParseWithMixedParams.command shouldBe "command"
        commandParseWithMixedParamsParams shouldContainExactly mapOf(
            "p1" to "v1",
            "param_2" to "v2",
            "p3" to "",
            "p4" to "v4",
            "p5" to "",
        )

        val commandParseForLastFullPair = bot.update.parseCommand("last_pair_command?v1&p2=v2")
        val commandParseForLastFullPairParams = bot.update.getParameters(
            DefaultArgParser::class,
            commandParseForLastFullPair,
        )
        commandParseForLastFullPair.command shouldBe "last_pair_command"
        commandParseForLastFullPairParams shouldContainExactly (mapOf("param_1" to "v1", "p2" to "v2"))

        bot.config.commandParsing.apply {
            commandDelimiter = '_'
        }

        val underscoreCommand = bot.update.parseCommand("/test_123")
        val underscoreCommandParams = bot.update.getParameters(DefaultArgParser::class, underscoreCommand)
        underscoreCommand.command shouldBe "/test"
        underscoreCommandParams shouldContainExactly (mapOf("param_1" to "123"))

        // deeplink checks

        bot.config.apply {
            commandParsing.restrictSpacesInCommands = true
        }
        val deeplinkParse = bot.update.parseCommand("/start deeplinkcode")
        val deeplinkParams = bot.update.getParameters(DefaultArgParser::class, deeplinkParse)
        deeplinkParse.command shouldBe "/start"
        deeplinkParams shouldContainExactly (mapOf("param_1" to "deeplinkcode"))

        bot.config.commandParsing.apply {
            commandDelimiter = ' '
            parametersDelimiter = ' '
            parameterValueDelimiter = ' '
            restrictSpacesInCommands = false
        }
        val deeplinkCheck = bot.update.parseCommand("/start bafefdf0-64cb-47da-97f0-4a1f11d469a2")
        val deeplinkCheckParams = bot.update.getParameters(DefaultArgParser::class, deeplinkCheck)
        deeplinkCheck.command shouldBe "/start"
        deeplinkCheckParams shouldContainExactly (mapOf("param_1" to "bafefdf0-64cb-47da-97f0-4a1f11d469a2"))

        bot.config.commandParsing = CommandParsingConfiguration()
        val defaultDeeplinkCheck = bot.update.parseCommand("/start default")
        val defaultDeeplinkCheckParams = bot.update.getParameters(DefaultArgParser::class, defaultDeeplinkCheck)
        defaultDeeplinkCheck.command shouldBe "/start"
        defaultDeeplinkCheckParams shouldContainExactly (mapOf("param_1" to "default"))
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
    fun `group command test`() {
        val text = "/test@KtGram?param1"
        bot.config.commandParsing.useIdentifierInGroupCommands = true

        bot.update.parseCommand(text).run {
            command shouldBe "/test"
            tail shouldBe "param1"
        }

        bot.update.parseCommand("/test@KtGram1?param1").run {
            command shouldBe "/test@KtGram1?param1"
            tail shouldBe ""
        }

        bot.config.commandParsing.useIdentifierInGroupCommands = false
        bot.update.parseCommand("/test@KtGram1?param1").run {
            command shouldBe "/test"
            tail shouldBe "param1"
        }
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
