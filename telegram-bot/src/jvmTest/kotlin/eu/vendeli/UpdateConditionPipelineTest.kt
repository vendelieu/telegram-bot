package eu.vendeli

import BotTestContext
import eu.vendeli.fixtures.ConditionCalls
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.common.Update
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.media.PhotoSize
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.common.processUpdate
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlin.random.Random
import kotlin.time.Instant

/**
 * Runs real updates through the whole pipeline against the annotation-style fixtures
 * (see `fixtures/ConditionHandlers.kt`, registered the way ktnip generates them).
 */
class UpdateConditionPipelineTest : BotTestContext(true, true) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    @BeforeEach
    fun resetCalls() = ConditionCalls.reset()

    @Test
    suspend fun `fixture handlers are registered exactly once`() {
        bot.update.registry
            .getUpdateTypeHandlers(eu.vendeli.tgbot.types.component.UpdateType.MESSAGE)
            .count { it.function == "widePhoto" } shouldBe 1
    }

    @Test
    suspend fun `annotation argument decides whether handler fires`() {
        bot.update.handle(photo(width = 900))
        ConditionCalls.fired.toList() shouldContainExactlyInAnyOrder listOf("widePhoto")

        ConditionCalls.reset()
        bot.update.handle(photo(width = 700))
        ConditionCalls.fired.shouldBeEmpty()
    }

    @Test
    suspend fun `handler without type annotation is gated by its condition alone`() {
        bot.update.handle(photo(width = 10, caption = "my receipt #1"))
        ConditionCalls.fired.toList() shouldContainExactlyInAnyOrder listOf("receiptPhoto")

        ConditionCalls.reset()
        bot.update.handle(photo(width = 10, caption = "something else"))
        ConditionCalls.fired.shouldBeEmpty()
    }

    @Test
    suspend fun `composed annotation requires every nested condition`() {
        bot.update.handle(photo(width = 600, caption = "Invoice 42"))
        ConditionCalls.fired.toList() shouldContainExactlyInAnyOrder listOf("wideInvoicePhoto")

        ConditionCalls.reset()
        bot.update.handle(photo(width = 600, caption = "no match"))
        ConditionCalls.fired.shouldBeEmpty()

        ConditionCalls.reset()
        bot.update.handle(photo(width = 100, caption = "Invoice 42"))
        ConditionCalls.fired.shouldBeEmpty()
    }

    @Test
    suspend fun `several handlers can fire for one update`() {
        bot.update.handle(photo(width = 900, caption = "receipt and invoice"))

        ConditionCalls.fired.toList() shouldContainExactlyInAnyOrder
            listOf("widePhoto", "receiptPhoto", "wideInvoicePhoto")
    }

    @Test
    suspend fun `conditions short-circuit in declaration order`() {
        // width fails first, so the caption matcher of the composed annotation must not be evaluated for it
        bot.update.handle(photo(width = 100, caption = "invoice"))

        ConditionCalls.evaluations.count { it.startsWith("CaptionContains(INVOICE)") } shouldBe 0
    }

    @Test
    suspend fun `non-photo message never reaches condition matchers`() {
        bot.update.handle(Update(Random.nextInt(), message = baseMessage().copy(text = "hi")).processUpdate())

        ConditionCalls.evaluations.shouldBeEmpty()
        ConditionCalls.fired.shouldBeEmpty()
    }

    private fun photo(width: Int, caption: String? = null): ProcessedUpdate = Update(
        Random.nextInt(),
        message = baseMessage().copy(photo = listOf(PhotoSize("id", "uid", width, 10)), caption = caption),
    ).processUpdate()

    private fun baseMessage() = Message(
        Random.nextLong(),
        from = User(1, false, "Test"),
        chat = Chat(1, ChatType.Private),
        date = Instant.fromEpochMilliseconds(0),
    )
}
