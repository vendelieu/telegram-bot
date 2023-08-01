package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.core.TelegramActionsCollector
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.utils.MockUpdate
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class AnnotationsTest : BotTestContext() {
    @Test
    fun `annotated methods scanning test`() {
        listOf("eu", "eu.vendeli", "other.pckg").forEach { pkg ->
            val actionsFromAnnotations = TelegramActionsCollector.collect(pkg)

            actionsFromAnnotations.commands.shouldNotBeEmpty()
            actionsFromAnnotations.regexCommands.shouldNotBeEmpty()
            actionsFromAnnotations.inputs.shouldNotBeEmpty()
            actionsFromAnnotations.unhandled.shouldNotBeNull()

            actionsFromAnnotations.commands.size shouldBe 3
            actionsFromAnnotations.regexCommands.size shouldBe 1
            actionsFromAnnotations.inputs.size shouldBe 3

            actionsFromAnnotations.commands.keys shouldContain "test"
            actionsFromAnnotations.commands.keys shouldContain "test2"
            actionsFromAnnotations.commands.keys shouldContain "test3"

            actionsFromAnnotations.regexCommands.keys.first() shouldBe "test colou?r".toRegex()

            actionsFromAnnotations.inputs.keys shouldContain "testInp"
            actionsFromAnnotations.inputs.keys shouldContain "testInp2"
            actionsFromAnnotations.inputs.keys shouldContain "testInp3"

            actionsFromAnnotations.updateHandlers.size shouldBe 2
            actionsFromAnnotations.updateHandlers[UpdateType.MESSAGE].shouldNotBeNull()
        }
    }

    @Test
    suspend fun `regex command test`() {
        doMockHttp(MockUpdate.SINGLE("test color"))

        bot.update.setListener {
            handle(it)
            stopListener()
        }

        val data = bot.userData.get<String>(0, "k")
        data shouldBe "test"
    }
}
