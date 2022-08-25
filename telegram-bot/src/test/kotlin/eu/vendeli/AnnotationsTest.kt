package eu.vendeli

import eu.vendeli.tgbot.core.TelegramActionsCollector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnnotationsTest {
    @ParameterizedTest
    @ValueSource(strings = ["eu", "eu.vendeli", "other.pckg"])
    fun `annotated methods scanning test`(pckg: String) {
        val actionsFromAnnotations = TelegramActionsCollector.collect(pckg)

        assertTrue(actionsFromAnnotations.commands.isNotEmpty())
        assertTrue(actionsFromAnnotations.inputs.isNotEmpty())
        assertNotNull(actionsFromAnnotations.unhandled)

        assertEquals(3, actionsFromAnnotations.commands.size)
        assertEquals(3, actionsFromAnnotations.inputs.size)

        assertTrue(actionsFromAnnotations.commands.keys.contains("test"))
        assertTrue(actionsFromAnnotations.commands.keys.contains("test2"))
        assertTrue(actionsFromAnnotations.commands.keys.contains("test3"))

        assertTrue(actionsFromAnnotations.inputs.keys.contains("testInp"))
        assertTrue(actionsFromAnnotations.inputs.keys.contains("testInp2"))
        assertTrue(actionsFromAnnotations.inputs.keys.contains("testInp3"))
    }
}