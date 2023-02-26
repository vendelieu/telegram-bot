package eu.vendeli

import eu.vendeli.tgbot.core.TelegramActionsCollector
import eu.vendeli.tgbot.types.internal.UpdateType
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AnnotationsTest {
    @ParameterizedTest
    @ValueSource(strings = ["eu", "eu.vendeli", "other.pckg"])
    fun `annotated methods scanning test`(pckg: String) {
        val actionsFromAnnotations = TelegramActionsCollector.collect(pckg)

        actionsFromAnnotations.commands.shouldNotBeEmpty()
        actionsFromAnnotations.inputs.shouldNotBeEmpty()
        actionsFromAnnotations.unhandled.shouldNotBeNull()

        actionsFromAnnotations.commands.size shouldBe 3
        actionsFromAnnotations.inputs.size shouldBe 3

        actionsFromAnnotations.commands.keys shouldContain "test"
        actionsFromAnnotations.commands.keys shouldContain "test2"
        actionsFromAnnotations.commands.keys shouldContain "test3"

        actionsFromAnnotations.inputs.keys shouldContain "testInp"
        actionsFromAnnotations.inputs.keys shouldContain "testInp2"
        actionsFromAnnotations.inputs.keys shouldContain "testInp3"

        actionsFromAnnotations.updateHandlers.size shouldBe 2
        actionsFromAnnotations.updateHandlers[UpdateType.MESSAGE].shouldNotBeNull()
    }
}
