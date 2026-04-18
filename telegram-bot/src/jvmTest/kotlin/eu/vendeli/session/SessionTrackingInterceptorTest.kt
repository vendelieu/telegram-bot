package eu.vendeli.session

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.session.Direction
import eu.vendeli.tgbot.types.session.SessionKey
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.runBlocking

/**
 * Verifies that registering `sessions { }` wires up the Setup-phase interceptor and every
 * incoming message-bearing update is deposited into its session. Tracking is performed via
 * an explicit [eu.vendeli.tgbot.types.component.ProcessingContext] parameter — no
 * `CoroutineContext` is involved.
 */
class SessionTrackingInterceptorTest : AnnotationSpec() {

    @Test
    fun setupInterceptorRecordsIncomingMessages() = runBlocking {
        val bot = TelegramBot("000000:TEST") {
            sessions { }
        }
        val mgr = checkNotNull(bot.sessions) { "sessions manager should be non-null after sessions { }" }

        val mock = eu.vendeli.utils.MockUpdate.SINGLE(text = "/track")
        val update = mock.updates.single()

        bot.update.handle(update)

        val session = mgr.of(update)!!
        session.key shouldBe SessionKey.ChatUser(chatId = 1, userId = 1)
        val tracked = session.messages()
        tracked.shouldHaveSize(1)
        tracked.single().direction shouldBe Direction.Incoming
        tracked.single().messageId shouldBe (update as MessageUpdate).message.messageId
    }

    @Test
    fun sessionsDisabledWhenBlockNotCalled() {
        val bot = TelegramBot("000000:TEST") { }
        bot.sessions shouldBe null
    }

    @Test
    fun trackIncomingFalseSuppressesAutoTracking() = runBlocking {
        val bot = TelegramBot("000000:TEST") {
            sessions { trackIncoming = false }
        }
        val mgr = checkNotNull(bot.sessions)

        val update = eu.vendeli.utils.MockUpdate.SINGLE().updates.single()
        bot.update.handle(update)

        mgr.of(update)!!.messages() shouldHaveSize 0
    }

    @Test
    fun managerFactoryOverrideIsUsed() {
        val sentinel = object : eu.vendeli.tgbot.interfaces.session.SessionManager {
            override fun get(chatId: Long, userId: Long?, qualifier: String?) =
                error("stub")
            override fun of(update: eu.vendeli.tgbot.types.component.ProcessedUpdate, qualifier: String?) =
                error("stub")
        }
        val bot = TelegramBot("000000:TEST") {
            sessions {
                managerFactory = eu.vendeli.tgbot.interfaces.session.SessionManagerFactory { _, _ -> sentinel }
            }
        }
        (bot.sessions === sentinel) shouldBe true
    }
}
