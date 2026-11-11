package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.ConditionOf
import eu.vendeli.tgbot.interfaces.helper.UpdateCondition
import eu.vendeli.tgbot.interfaces.helper.UpdateMatcher
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.common.Update
import eu.vendeli.tgbot.types.component.MessageKind
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.media.PhotoSize
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.common.processUpdate
import eu.vendeli.tgbot.utils.common.safeCast
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.shouldBe
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random
import kotlin.time.Instant

@ConditionOf(MinWidthMatcher::class)
private annotation class MinWidth(
    val value: Int,
)

private object MinWidthMatcher : UpdateMatcher<MinWidth> {
    override suspend fun matches(annotation: MinWidth, update: ProcessedUpdate, bot: TelegramBot): Boolean =
        update
            .safeCast<MessageUpdate>()
            ?.message
            ?.photo
            ?.any { it.width >= annotation.value } == true
}

class UpdateConditionTest : BotTestContext(true, true) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    @Test
    suspend fun `matcher bound to annotation arguments gates the handler`() {
        val fired = AtomicInteger(0)
        bot.update.registry.clear()
        bot.update.functionalDsl.onUpdate(
            UpdateType.MESSAGE,
            messageKind = setOf(MessageKind.PHOTO),
            conditions = listOf(MinWidthMatcher.bind(MinWidth(800))),
        ) { fired.incrementAndGet() }

        dispatch(photo(width = 1000), photo(width = 100))

        fired.get() shouldBe 1
    }

    @Test
    suspend fun `all conditions must match`() {
        val fired = AtomicInteger(0)
        bot.update.registry.clear()
        bot.update.functionalDsl.onUpdate(
            UpdateType.MESSAGE,
            conditions = listOf(
                MinWidthMatcher.bind(MinWidth(50)),
                MinWidthMatcher.bind(MinWidth(800)),
            ),
        ) { fired.incrementAndGet() }

        dispatch(photo(width = 100), photo(width = 900))

        fired.get() shouldBe 1
    }

    @Test
    suspend fun `conditions are not evaluated when message kind already excludes the update`() {
        val evaluated = AtomicInteger(0)
        bot.update.registry.clear()
        bot.update.functionalDsl.onUpdate(
            UpdateType.MESSAGE,
            messageKind = setOf(MessageKind.PHOTO),
            conditions = listOf(UpdateCondition { _, _ -> evaluated.incrementAndGet() >= 0 }),
        ) { }

        dispatch(text("hi"))

        evaluated.get() shouldBe 0
    }

    @Test
    suspend fun `throwing condition counts as not matched and does not affect other handlers`() {
        val conditional = AtomicInteger(0)
        val plain = AtomicInteger(0)
        bot.update.registry.clear()
        bot.update.functionalDsl.apply {
            onUpdate(UpdateType.MESSAGE, conditions = listOf(UpdateCondition { _, _ -> error("boom") })) {
                conditional.incrementAndGet()
            }
            onUpdate(UpdateType.MESSAGE) { plain.incrementAndGet() }
        }

        dispatch(text("hi"))

        conditional.get() shouldBe 0
        plain.get() shouldBe 1
    }

    @Test
    suspend fun `handlers differing only by condition are registered separately`() {
        val a = AtomicInteger(0)
        val b = AtomicInteger(0)
        bot.update.registry.clear()
        bot.update.functionalDsl.apply {
            onUpdate(UpdateType.MESSAGE, conditions = listOf(UpdateCondition { _, _ -> true })) { a.incrementAndGet() }
            onUpdate(UpdateType.MESSAGE, conditions = listOf(UpdateCondition { _, _ -> true })) { b.incrementAndGet() }
        }

        dispatch(text("hi"))

        a.get() shouldBe 1
        b.get() shouldBe 1
    }

    @Test
    suspend fun `kind-only lookup does not evaluate conditions`() {
        bot.update.registry.clear()
        bot.update.functionalDsl.onUpdate(
            UpdateType.MESSAGE,
            conditions = listOf(UpdateCondition { _, _ -> false }),
        ) { }

        bot.update.registry
            .getUpdateTypeHandlers(text("hi"))
            .size shouldBe 1
    }

    private suspend fun dispatch(vararg updates: ProcessedUpdate) = updates.forEach { update ->
        val ctx = ProcessingContext(update, bot, bot.update.registry)
        bot.update.registry
            .getUpdateTypeHandlers(ctx)
            .forEach { it.invoke(ctx) }
    }

    private fun photo(width: Int) = Update(
        Random.nextInt(),
        message = baseMessage().copy(photo = listOf(PhotoSize("id", "uid", width, 10))),
    ).processUpdate()

    private fun text(value: String) = Update(
        Random.nextInt(),
        message = baseMessage().copy(text = value),
    ).processUpdate()

    private fun baseMessage() = Message(
        Random.nextLong(),
        from = User(1, false, "Test"),
        chat = Chat(1, ChatType.Private),
        date = Instant.fromEpochMilliseconds(0),
    )
}
