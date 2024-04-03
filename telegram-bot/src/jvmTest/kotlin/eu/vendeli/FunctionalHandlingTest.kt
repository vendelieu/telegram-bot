package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.ActivityCtx
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.types.internal.userOrNull
import eu.vendeli.tgbot.utils.processUpdate
import eu.vendeli.utils.MockUpdate
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.datetime.Instant
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class FunctionalHandlingTest : BotTestContext(true, true) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    @ExperimentalKotest
    override fun concurrency(): Int = 1

    @Test
    suspend fun `input chaining`() {
        doMockHttp()
        val loopCounter = AtomicInteger(0)

        bot.inputListener.set(1, "test")
        bot.handleUpdates {
            inputChain("test") {
                update.userOrNull?.id shouldBe 1
            }.breakIf(
                { update !is MessageUpdate },
                false,
            ) {
                (update as? MessageUpdate)?.message?.chat?.id shouldBe 1
            }.andThen {
                update.userOrNull?.id shouldBe 2
                // processing should not reach this point so the check will not fail our test
            }.andThen {
                println(this)
            }.breakIf(
                { true },
            ) {
                println(this)
            }

            if (loopCounter.incrementAndGet() == 5) bot.update.stopListener()
        }

        bot.update.caughtExceptions.tryReceive().getOrNull().shouldBeNull()
    }

    @Test
    suspend fun `input chaining repeating`() {
        val generalCounter = AtomicInteger(0)
        val firstChainCounter = AtomicInteger(0)
        val breakCounter = AtomicInteger(0)
        val secondChainCounter = AtomicInteger(0)

        bot.inputListener.set(1, "test")
        bot.handleUpdates {
            inputChain("test") {
                firstChainCounter.incrementAndGet()
                // first handling (because we set listener before handler start) + 5 general entries
                update.userOrNull?.id shouldBe 1
            }.breakIf({ generalCounter.get() < 3 }) {
                breakCounter.incrementAndGet()
                (update as? MessageUpdate)?.message?.chat?.id shouldBe 1
            }.andThen {
                secondChainCounter.incrementAndGet()
                (update as? MessageUpdate)?.message?.text shouldBe "/start"
            }

            if (generalCounter.incrementAndGet() == 5) bot.update.stopListener()
            delay(1)
        }

        generalCounter.get() shouldBe 5
        firstChainCounter.get() shouldBe 1
        breakCounter.get() shouldBe 1
        secondChainCounter.get() shouldBe 2
        bot.update.caughtExceptions.tryReceive().getOrNull().shouldBeNull()
    }

    @Test
    suspend fun `whenNotHandled reaching test`() {
        val generalCounter = AtomicInteger(0)
        val startCounter = AtomicInteger(0)
        val notHandledCounter = AtomicInteger(0)

        doMockHttp(MockUpdate.TEXT_LIST(listOf("test", "/start")))

        bot.handleUpdates {
            onCommand("/start") {
                startCounter.incrementAndGet()
            }

            whenNotHandled {
                notHandledCounter.incrementAndGet()
            }

            if (generalCounter.incrementAndGet() == 5) bot.update.stopListener()
        }

        generalCounter.get() shouldBeGreaterThanOrEqual 5
        startCounter.get() shouldBeGreaterThanOrEqual 2
        notHandledCounter.get() shouldBe 3
    }

    @Test
    suspend fun `functional activities setting test`() {
        val ctx = ActivityCtx(
            Update(
                Random.nextInt(),
                editedMessage = Message(
                    Random.nextLong(),
                    from = User(1, false, "Test"),
                    chat = Chat(1, ChatType.Private),
                    date = Instant.fromEpochMilliseconds(0),
                    text = "text",
                ),
            ).processUpdate(),
        )

        var onUpdateInvocationsCount = 0

        bot.update.functionalHandlingBehavior.functionalActivities.onUpdateActivities.clear()
        bot.update.functionalHandlingBehavior.apply {
            onMessage { onUpdateInvocationsCount++ }
            onEditedMessage { onUpdateInvocationsCount++ }
            onChannelPost { onUpdateInvocationsCount++ }
            onEditedChannelPost { onUpdateInvocationsCount++ }
            onMessageReaction { onUpdateInvocationsCount++ }
            onMessageReactionCount { onUpdateInvocationsCount++ }
            onInlineQuery { onUpdateInvocationsCount++ }
            onChosenInlineResult { onUpdateInvocationsCount++ }
            onCallbackQuery { onUpdateInvocationsCount++ }
            onShippingQuery { onUpdateInvocationsCount++ }
            onPreCheckoutQuery { onUpdateInvocationsCount++ }
            onPoll { onUpdateInvocationsCount++ }
            onPollAnswer { onUpdateInvocationsCount++ }
            onMyChatMember { onUpdateInvocationsCount++ }
            onChatMember { onUpdateInvocationsCount++ }
            onChatJoinRequest { onUpdateInvocationsCount++ }
            onChatBoost { onUpdateInvocationsCount++ }
            onRemovedChatBoost { onUpdateInvocationsCount++ }
            onBusinessConnection { onUpdateInvocationsCount++ }
            onBusinessMessage { onUpdateInvocationsCount++ }
            onEditedBusinessMessage { onUpdateInvocationsCount++ }
            onDeletedBusinessMessages { onUpdateInvocationsCount++ }
        }

        UpdateType.entries.forEach {
            shouldNotThrowAny {
                bot.update.functionalHandlingBehavior.functionalActivities
                    .onUpdateActivities[it].shouldNotBeNull().invoke(ctx)
            }
        }

        delay(1)
        onUpdateInvocationsCount shouldBe UpdateType.entries.size

        bot.update.functionalHandlingBehavior.apply {
            val regex = "^*.".toRegex()
            onCommand(
                regex,
                setOf(UpdateType.CALLBACK_QUERY),
                RateLimits.NOT_LIMITED,
            ) { }
            functionalActivities.regexCommands[regex].shouldNotBeNull()

            onInput("test") { }
            functionalActivities.inputs["test"].shouldNotBeNull()
        }
    }
}
