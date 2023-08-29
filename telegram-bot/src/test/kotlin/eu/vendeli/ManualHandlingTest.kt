package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.internal.CommandScope
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.utils.MockUpdate
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.util.concurrent.atomic.AtomicInteger

class ManualHandlingTest : BotTestContext(true, true) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    @Test
    suspend fun `input chaining`() {
        val loopCounter = AtomicInteger(0)

        bot.inputListener.set(1, "test")
        bot.handleUpdates {
            inputChain("test") {
                user.id shouldBe 1
            }.breakIf(
                { update.message != null },
                false,
            ) {
                update.message?.chat?.id shouldBe 1
            }.andThen {
                user.id shouldBe 2
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
                firstChainCounter.incrementAndGet() shouldBeLessThanOrEqual 6
                // first handling (because we set listener before handler start) + 5 general entries
                user.id shouldBe 1
            }.breakIf(
                { generalCounter.get() < 3 },
            ) {
                breakCounter.incrementAndGet() shouldBeLessThan 3
                update.message?.chat?.id shouldBe 1
            }.andThen {
                secondChainCounter.incrementAndGet() shouldBe 1
                update.message?.text shouldBe "/start"
            }

            if (generalCounter.incrementAndGet() == 5) bot.update.stopListener()
        }

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

        generalCounter.get() shouldBe 6
        startCounter.get() shouldBe 3
        notHandledCounter.get() shouldBe 3
    }

    @Test
    suspend fun `manual actions setting test`() {
        bot.handleUpdates {
            manualActions.onEditedMessage.shouldBeNull()
            onEditedMessage {}
            should { manualActions.onEditedMessage }.shouldNotBeNull()

            manualActions.onPollAnswer.shouldBeNull()
            onPollAnswer {}
            should { manualActions.onPollAnswer }.shouldNotBeNull()

            manualActions.onCallbackQuery.shouldBeNull()
            onCallbackQuery {}
            should { manualActions.onCallbackQuery }.shouldNotBeNull()

            manualActions.onPoll.shouldBeNull()
            onPoll {}
            should { manualActions.onPoll }.shouldNotBeNull()

            manualActions.onChatJoinRequest.shouldBeNull()
            onChatJoinRequest {}
            should { manualActions.onChatJoinRequest }.shouldNotBeNull()

            manualActions.onChatMember.shouldBeNull()
            onChatMember {}
            should { manualActions.onChatMember }.shouldNotBeNull()

            manualActions.onMyChatMember.shouldBeNull()
            onMyChatMember {}
            should { manualActions.onMyChatMember }.shouldNotBeNull()

            manualActions.onChannelPost.shouldBeNull()
            onChannelPost {}
            should { manualActions.onChannelPost }.shouldNotBeNull()

            manualActions.onEditedChannelPost.shouldBeNull()
            onEditedChannelPost {}
            should { manualActions.onEditedChannelPost }.shouldNotBeNull()

            manualActions.onChosenInlineResult.shouldBeNull()
            onChosenInlineResult {}
            should { manualActions.onChosenInlineResult }.shouldNotBeNull()

            manualActions.onInlineQuery.shouldBeNull()
            onInlineQuery {}
            should { manualActions.onInlineQuery }.shouldNotBeNull()

            manualActions.onPreCheckoutQuery.shouldBeNull()
            onPreCheckoutQuery {}
            should { manualActions.onPreCheckoutQuery }.shouldNotBeNull()

            manualActions.onShippingQuery.shouldBeNull()
            onShippingQuery {}
            should { manualActions.onShippingQuery }.shouldNotBeNull()

            manualActions.regexCommands.shouldBeEmpty()
            onCommand("^*.".toRegex(), setOf(CommandScope.CALLBACK), RateLimits.NOT_LIMITED) {}
            manualActions.regexCommands.size shouldBe 1

            manualActions.onInput.shouldBeEmpty()
            onInput("test") {}
            manualActions.onInput.size shouldBe 1

            bot.update.stopListener()
        }
    }
}
