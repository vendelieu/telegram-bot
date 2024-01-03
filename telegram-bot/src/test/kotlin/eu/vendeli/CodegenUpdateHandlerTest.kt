package eu.vendeli

import BotTestContext
import eu.vendeli.fixtures.`$ACTIONS_eu_vendeli_fixtures`
import eu.vendeli.tgbot.core.CodegenUpdateHandler
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.IsolationMode
import io.mockk.every

class CodegenUpdateHandlerTest : BotTestContext() {
    override fun isolationMode() = IsolationMode.InstancePerLeaf

    @ExperimentalKotest
    override fun concurrency(): Int = 1

    private val updateHandlerTest = TelegramUpdateHandlerTest().also {
        it.prepareTestBot()
        it.spykIt()
        every { it.bot.update } returns CodegenUpdateHandler(`$ACTIONS_eu_vendeli_fixtures`, it.bot)
    }

    @Test
    suspend fun deeplinkTest() = updateHandlerTest.`deeplink test`()

    @Test
    suspend fun commandOverInputPriorityTest() = updateHandlerTest.`command over input priority test`()

    @Test
    suspend fun exceptionCatchingAnnotation() = updateHandlerTest.`exception catching via annotation handling`()

    @Test
    suspend fun exceptionCatchingManual() = updateHandlerTest.`exception catching via manual handling`()

    @Test
    suspend fun inputMediaHandling() = updateHandlerTest.`input media handling test`()

    @Test
    suspend fun listenerWorkflow() = updateHandlerTest.`listener workflow`()

    @Test
    suspend fun validCommandParsing() = updateHandlerTest.`valid command parsing`()

    @Test
    suspend fun webhookHandling() = updateHandlerTest.`webhook handling test`()
}
