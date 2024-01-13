package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.media.Document
import eu.vendeli.tgbot.utils.parseActivity
import eu.vendeli.utils.MockUpdate
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlinx.coroutines.delay
import java.time.Instant

class TelegramUpdateHandlerTest : BotTestContext() {
    override fun isolationMode() = IsolationMode.InstancePerLeaf

    @ExperimentalKotest
    override fun concurrency(): Int = 1

    @Test
    suspend fun `listener workflow`() {
        doMockHttp(MockUpdate.RAW_RESPONSE(updates))

        var update: Update? = null

        bot.update.setListener {
            update = it
            stopListener()
        }

        update.shouldNotBeNull()
        update?.updateId shouldBe 53192527
        update?.message?.from?.username shouldBe "username"
        update?.message?.from?.firstName shouldBe "John Doe"
    }

    @Test
    suspend fun `exception catching via manual handling`() {
        doMockHttp()

        bot.update.caughtExceptions.tryReceive().getOrNull().shouldBeNull()

        bot.handleUpdates {
            onMessage {
                throw NoSuchElementException("test")
            }
            bot.update.stopListener()
        }
        val throwableUpdatePair = bot.update.caughtExceptions.tryReceive().getOrNull()
        throwableUpdatePair.shouldNotBeNull()

        throwableUpdatePair.exception shouldNotBeSameInstanceAs NoSuchElementException::class
        throwableUpdatePair.exception.message shouldBe "test"

        throwableUpdatePair.update.message.shouldNotBeNull()
        throwableUpdatePair.update.message?.text shouldBe "/start"
    }

    @Test
    suspend fun `exception catching via annotation handling`() {
        doMockHttp(MockUpdate.SINGLE("test"))

        bot.update.caughtExceptions.tryReceive().getOrNull().shouldBeNull()
        bot.update.setListener {
            handle(it)
            stopListener()
        }

        val throwableUpdatePair = bot.update.caughtExceptions.tryReceive().getOrNull()
        throwableUpdatePair.shouldNotBeNull()

        throwableUpdatePair.exception shouldNotBeSameInstanceAs IllegalArgumentException::class
        throwableUpdatePair.exception.message shouldBe "test2"

        throwableUpdatePair.update.message.shouldNotBeNull()
        throwableUpdatePair.update.message?.text shouldBe "test"
    }

    @Test
    fun `valid command parsing`() {
        bot.config.apply {
            commandParsing.restrictSpacesInCommands = true
        }
        val deeplinkParse = bot.update.parseActivity("/start deeplinkcode")
        deeplinkParse.command shouldBe "/start"
        deeplinkParse.params shouldContainExactly (mapOf("param_1" to "deeplinkcode"))

        val commandParseWithNoParams = bot.update.parseActivity("/command")
        commandParseWithNoParams.command shouldBe "/command"
        commandParseWithNoParams.params.size shouldBe 0

        val commandParseWithOneEmptyParam = bot.update.parseActivity("/command?")
        commandParseWithOneEmptyParam.command shouldBe "/command"
        commandParseWithOneEmptyParam.params shouldContainExactly (mapOf("param_1" to ""))

        val commandParseWithMixedParams = bot.update.parseActivity("command?p1=v1&v2&p3=&p4=v4&p5=")
        commandParseWithMixedParams.command shouldBe "command"
        commandParseWithMixedParams.params shouldContainExactly mapOf(
            "p1" to "v1",
            "param_2" to "v2",
            "p3" to "",
            "p4" to "v4",
            "p5" to "",
        )

        val commandParseForLastFullPair = bot.update.parseActivity("last_pair_command?v1&p2=v2")
        commandParseForLastFullPair.command shouldBe "last_pair_command"
        commandParseForLastFullPair.params shouldContainExactly (mapOf("param_1" to "v1", "p2" to "v2"))

        bot.config.commandParsing.apply {
            commandDelimiter = '_'
        }

        val underscoreCommand = bot.update.parseActivity("/test_123")
        underscoreCommand.command shouldBe "/test"
        underscoreCommand.params shouldContainExactly (mapOf("param_1" to "123"))

        bot.config.commandParsing.apply {
            commandDelimiter = ' '
            parametersDelimiter = ' '
            parameterValueDelimiter = ' '
            restrictSpacesInCommands = false
        }
        val deeplinkCheck = bot.update.parseActivity("/start bafefdf0-64cb-47da-97f0-4a1f11d469a2")
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
            if (it.message?.text == "aaaa") stopListener()
        }

        bot.update.caughtExceptions.tryReceive().getOrNull().shouldNotBeNull()
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
                            date = Instant.EPOCH,
                            chat = Chat(1, ChatType.Private),
                            document = Document("3", "33"),
                        ),
                    ),
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
        val rawUpdate = MockUpdate.SINGLE().response.toString(Charsets.UTF_8)
        var update: Update? = null
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
