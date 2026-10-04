package eu.vendeli

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.utils.common.onMessage
import eu.vendeli.tgbot.utils.common.serde
import eu.vendeli.utils.MockUpdate
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Update handling on a virtual clock: both bot dispatchers are driven by the test scheduler, so there are no
 * sleeps, no real threads and the outcome does not depend on machine speed.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UpdateHandlerVirtualTimeTest : AnnotationSpec() {
    private fun TestScope.virtualBot(): TelegramBot {
        val dispatcher = StandardTestDispatcher(testScheduler)
        return TelegramBot("000000:TEST") {
            updatesListener {
                this.dispatcher = dispatcher
                processingDispatcher = dispatcher
            }
        }
    }

    private fun rawUpdate(text: String): String = serde.encodeToString(
        ProcessedUpdate.serializer(),
        MockUpdate.SINGLE(text).updates.single(),
    )

    @Test
    fun parseAndHandleRunsOnlyWhenTheDispatcherIsAdvanced() = runTest {
        val bot = virtualBot()
        val received = CopyOnWriteArrayList<ProcessedUpdate>()
        bot.update.setBehaviour { received += it }

        val job = bot.update.parseAndHandle(rawUpdate("/start"))

        received.shouldBeEmpty()
        job.isCompleted.shouldBeFalse()
        advanceUntilIdle()
        job.isCompleted.shouldBeTrue()
        received.shouldHaveSize(1)
        received.single().text shouldBe "/start"
    }

    @Test
    fun aFailingHandlerIsReportedAndDoesNotStopLaterUpdates() = runTest {
        val bot = virtualBot()
        val handled = CopyOnWriteArrayList<String>()
        bot.setFunctionality {
            onMessage {
                val text = update.message.text.orEmpty()
                if (text == "boom") error("handler failure")
                handled += text
            }
        }
        bot.update.setBehaviour { handle(it) }

        bot.update.parseAndHandle(rawUpdate("boom"))
        bot.update.parseAndHandle(rawUpdate("fine"))
        advanceUntilIdle()

        handled.toList() shouldBe listOf("fine")
        bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
            .shouldNotBeNull()
            .exception.message shouldBe "handler failure"
    }

    @Test
    fun malformedUpdatesAreDroppedWithoutInvokingTheHandler() = runTest {
        val bot = virtualBot()
        val received = CopyOnWriteArrayList<ProcessedUpdate>()
        bot.update.setBehaviour { received += it }

        val job = bot.update.parseAndHandle("{not json")
        advanceUntilIdle()

        job.isCompleted.shouldBeTrue()
        received.shouldBeEmpty()
    }

    @Test
    fun updatesAreHandledInTheOrderTheyWereSubmitted() = runTest {
        val bot = virtualBot()
        val handled = CopyOnWriteArrayList<String>()
        bot.update.setBehaviour { handled += it.text }

        listOf("a", "b", "c").forEach { bot.update.parseAndHandle(rawUpdate(it)) }
        advanceUntilIdle()

        handled.toList() shouldBe listOf("a", "b", "c")
    }
}
