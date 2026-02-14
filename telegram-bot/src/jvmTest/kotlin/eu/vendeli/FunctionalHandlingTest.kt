package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.common.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.component.ActivityCtx
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.types.component.userOrNull
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.common.onBusinessConnection
import eu.vendeli.tgbot.utils.common.onBusinessMessage
import eu.vendeli.tgbot.utils.common.onCallbackQuery
import eu.vendeli.tgbot.utils.common.onChannelPost
import eu.vendeli.tgbot.utils.common.onChatBoost
import eu.vendeli.tgbot.utils.common.onChatJoinRequest
import eu.vendeli.tgbot.utils.common.onChatMember
import eu.vendeli.tgbot.utils.common.onChosenInlineResult
import eu.vendeli.tgbot.utils.common.onDeletedBusinessMessages
import eu.vendeli.tgbot.utils.common.onEditedBusinessMessage
import eu.vendeli.tgbot.utils.common.onEditedChannelPost
import eu.vendeli.tgbot.utils.common.onEditedMessage
import eu.vendeli.tgbot.utils.common.onInlineQuery
import eu.vendeli.tgbot.utils.common.onMessage
import eu.vendeli.tgbot.utils.common.onMessageReaction
import eu.vendeli.tgbot.utils.common.onMessageReactionCount
import eu.vendeli.tgbot.utils.common.onMyChatMember
import eu.vendeli.tgbot.utils.common.onPoll
import eu.vendeli.tgbot.utils.common.onPollAnswer
import eu.vendeli.tgbot.utils.common.onPreCheckoutQuery
import eu.vendeli.tgbot.utils.common.onPurchasedPaidMedia
import eu.vendeli.tgbot.utils.common.onRemovedChatBoost
import eu.vendeli.tgbot.utils.common.onShippingQuery
import eu.vendeli.tgbot.utils.common.processUpdate
import eu.vendeli.utils.MockUpdate
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlin.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class FunctionalHandlingTest : BotTestContext(true, true) {
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    @Test
    suspend fun `input chaining`() {
        doMockHttp()
        val loopCounter = AtomicInteger(0)

        bot.inputListener.set(1, "test")
        bot.setFunctionality {
            inputChain("test") {
                update.userOrNull?.id shouldBe 1
            }.breakIf(
                { update is MessageUpdate },
                false,
            ) {
                (update as? MessageUpdate)?.message?.chat?.id shouldBe 1
            }.andThen {
                update.userOrNull?.id shouldBe 2
                // processing should not reach this point, so the check will not fail our test
            }.andThen {
                println(this)
            }.breakIf(
                { true },
            ) {
                println(this)
            }
        }
        bot.update.setListener {
            handle(it)
            if (loopCounter.incrementAndGet() == 5) bot.update.stopListener()
        }

        bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
            .shouldBeNull()
    }

    @Test
    suspend fun `input chaining repeating`() {
        val generalCounter = AtomicInteger(0)
        val firstChainCounter = AtomicInteger(0)
        val breakCounter = AtomicInteger(0)
        val secondChainCounter = AtomicInteger(0)

        bot.inputListener.set(1, "test")
        bot.setFunctionality {
            inputChain("test") {
                firstChainCounter.incrementAndGet()
                update.userOrNull?.id shouldBe 1
            }.breakIf({ generalCounter.get() < 3 }) {
                breakCounter.incrementAndGet()
                (update as? MessageUpdate)?.message?.chat?.id shouldBe 1
            }.andThen {
                secondChainCounter.incrementAndGet()
                (update as? MessageUpdate)?.message?.text shouldBe "/start"
            }
        }
        bot.update.setListener {
            val steps = StringBuilder()
            steps.append(bot.inputListener.get(1))
            handle(it)
            steps.append(" -> ")
            steps.append(bot.inputListener.get(1))
            println(steps)
            if (generalCounter.incrementAndGet() == 5) bot.update.stopListener()
            delay(1)
        }

        generalCounter.get() shouldBe 5
        firstChainCounter.get() shouldBe 4
        breakCounter.get() shouldBe 3
        secondChainCounter.get() shouldBe 1
        bot.update.caughtExceptions
            .tryReceive()
            .getOrNull()
            .shouldBeNull()
    }

    @Test
    suspend fun `whenNotHandled reaching test`() {
        val generalCounter = AtomicInteger(0)
        var startCounter = false
        var notHandledCounter = false

        doMockHttp(MockUpdate.TEXT_LIST("test", "/start"))

        bot.update.registry.clear()
        bot.setFunctionality {
            onCommand("/start") {
                startCounter = true
            }

            whenNotHandled {
                notHandledCounter = true
            }
        }
        bot.update.setListener {
            handle(it)
            if (generalCounter.incrementAndGet() == 5) bot.update.stopListener()
        }

        generalCounter.get() shouldBeGreaterThanOrEqual 5
        startCounter.shouldBeTrue()
        notHandledCounter.shouldBeTrue()
    }

    @Test
    suspend fun `common handler reaching test`() {
        val generalCounter = AtomicInteger(0)
        var startReached = false
        var commonReached = false
        var regexCommonReached = false

        doMockHttp(MockUpdate.TEXT_LIST("test", "/start", "123"))

        bot.update.registry.clear()
        bot.setFunctionality {
            onCommand("/start") {
                startReached = true
            }

            common("test") {
                commonReached = true
            }

            common("^\\d+\$".toRegex()) {
                regexCommonReached = true
            }
        }
        bot.update.setListener {
            handle(it)
            if (generalCounter.incrementAndGet() == 5) bot.update.stopListener()
        }

        startReached.shouldBeTrue()
        commonReached.shouldBeTrue()
        regexCommonReached.shouldBeTrue()
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

        bot.update.registry.clear()
        bot.update.functionalDsl.apply {
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
            onPurchasedPaidMedia { onUpdateInvocationsCount++ }
        }

        UpdateType.entries.forEach { type ->
            shouldNotThrowAny {
                val handlers = bot.update.registry.getUpdateTypeHandlers(type)
                handlers.forEach {
                    it.invoke(ProcessingContext(ctx.update, bot, bot.update.registry))
                }
            }
        }

        delay(1)
        onUpdateInvocationsCount shouldBe UpdateType.entries.size

        bot.update.functionalDsl.apply {
            val scope = setOf(UpdateType.MESSAGE)
            val regex = "^*.".toRegex()
            common(
                regex,
                scope = scope,
                rateLimits = RateLimits.NOT_LIMITED,
            ) { }

            bot.update.registry
                .findCommonHandler(
                    "t",
                    ProcessingContext(MockUpdate.SINGLE("t").updates.first(), bot, bot.update.registry),
                ).shouldNotBeNull()

            onInput("test") { }
            bot.update.registry
                .findInput("test")
                .shouldNotBeNull()
        }
    }
}
