package eu.vendeli.session

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.session.Direction
import eu.vendeli.tgbot.types.session.SessionKey
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking

/**
 * Verifies the predicate-based tracking interceptor: idle bots store nothing,
 * opening a session via `bot.sessions.of(...)` subscribes the resolved key, and the
 * interceptor only writes for keys with live subscriptions. `Session.close()` reverses
 * the subscription and drops the storage bucket.
 */
class SessionTrackingInterceptorTest : AnnotationSpec() {
    @Test
    fun setupInterceptorRecordsIncomingMessagesForOpenSessions() = runBlocking {
        val bot = TelegramBot("000000:TEST")
        val update = eu.vendeli.utils.MockUpdate
            .SINGLE(text = "/track")
            .updates
            .single()

        // Open the session before the interceptor runs (subscribes the resolved key).
        val session = bot.sessions.of(update)!!

        bot.update.handle(update)

        session.key shouldBe SessionKey.ChatUser(chatId = 1, userId = 1)
        val tracked = session.messages()
        tracked.shouldHaveSize(1)
        tracked.single().direction shouldBe Direction.Incoming
        tracked.single().messageId shouldBe (update as MessageUpdate).message.messageId
    }

    @Test
    fun interceptorIsNoOpWhenNoSubscriptions() = runBlocking {
        val bot = TelegramBot("000000:TEST")
        val update = eu.vendeli.utils.MockUpdate
            .SINGLE()
            .updates
            .single()

        bot.sessions.isIdle() shouldBe true
        bot.update.handle(update)

        // Idle path: nothing was tracked. of() now subscribes — verify the freshly opened
        // session has no entries.
        bot.sessions
            .of(update)!!
            .messages()
            .shouldBeEmpty()
    }

    @Test
    fun interceptorOnlyTracksMatchingSubscriptions() = runBlocking {
        val bot = TelegramBot("000000:TEST")
        val update = eu.vendeli.utils.MockUpdate
            .SINGLE()
            .updates
            .single()

        // Open a session for a different chat — its predicate will not match the incoming update.
        val unrelated = bot.sessions.get(chatId = 999_999L, userId = 999_999L)

        bot.update.handle(update)

        unrelated.messages().shouldBeEmpty()
    }

    @Test
    fun closeRemovesSubscriptionAndClearsStorage() = runBlocking {
        val bot = TelegramBot("000000:TEST")
        val update = eu.vendeli.utils.MockUpdate
            .SINGLE(text = "/track")
            .updates
            .single()

        val session = bot.sessions.of(update)!!
        bot.update.handle(update)
        session.messages() shouldHaveSize 1

        session.close()

        bot.sessions.isIdle() shouldBe true
        // After close, a freshly resolved session for the same key starts empty.
        bot.sessions
            .of(update)!!
            .messages()
            .shouldBeEmpty()
    }

    @Test
    fun managerFactoryOverrideIsUsed() {
        val sentinel = object : eu.vendeli.tgbot.interfaces.session.SessionManager {
            override fun get(chatId: Long, userId: Long?, qualifier: String?) = error("stub")
            override fun of(update: eu.vendeli.tgbot.types.component.ProcessedUpdate, qualifier: String?) =
                error("stub")
            override fun subscribe(
                key: SessionKey,
                predicate: (eu.vendeli.tgbot.types.component.ProcessedUpdate) -> Boolean,
            ) = error("stub")
            override fun unsubscribe(key: SessionKey) = error("stub")
            override fun matchingSubscriptions(update: eu.vendeli.tgbot.types.component.ProcessedUpdate) =
                emptyList<SessionKey>()
            override fun isIdle(): Boolean = true
        }
        val bot = TelegramBot("000000:TEST") {
            sessions {
                managerFactory = eu.vendeli.tgbot.interfaces.session
                    .SessionManagerFactory { _, _ -> sentinel }
            }
        }
        (bot.sessions === sentinel) shouldBe true
    }
}
