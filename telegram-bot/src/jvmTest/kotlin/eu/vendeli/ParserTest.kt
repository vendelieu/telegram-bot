package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.types.internal.configuration.CommandParsingConfiguration
import eu.vendeli.tgbot.utils.getParameters
import eu.vendeli.tgbot.utils.parseCommand
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlin.time.measureTime

class ParserTest : BotTestContext() {
    @Test
    fun `group command test`() = measureTime {
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
    }.also {
        println("--------")
        println(it)
        println("--------")
    }

    @Test
    fun `valid command parsing`() = measureTime {
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
    }.also {
        println("--------")
        println(it)
        println("--------")
    }
}
