package eu.vendeli.session

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.session.Direction
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import kotlin.time.Instant

/**
 * End-to-end cover for [eu.vendeli.tgbot.interfaces.session.Session] track/forget semantics.
 * Uses [TelegramBot] with the in-memory default storage — no HTTP is involved, so this
 * stays a fast unit-level check.
 */
class SessionTrackTest : AnnotationSpec() {
    private fun mkBot() = TelegramBot("000000:TEST") { sessions { trackIncoming = false } }

    private fun mkMessage(id: Long) = Message(
        messageId = id,
        from = User(id = 20, isBot = false, firstName = "U"),
        chat = Chat(id = 10, type = ChatType.Private),
        date = Instant.DISTANT_PAST,
        text = "m$id",
    )

    @Test
    fun trackRecordsOutgoingAndIncoming() = runBlocking {
        val bot = mkBot()
        val session = bot.sessions!!.get(chatId = 10, userId = 20)

        session.track(mkMessage(1), Direction.Outgoing)
        session.track(mkMessage(2), Direction.Incoming)

        val all = session.messages()
        all.shouldHaveSize(2)
        all
            .map { it.messageId to it.direction }
            .toSet() shouldBe setOf(1L to Direction.Outgoing, 2L to Direction.Incoming)
    }

    @Test
    fun forgetAppliesPredicate() = runBlocking {
        val bot = mkBot()
        val session = bot.sessions!!.get(chatId = 10, userId = 20)

        repeat(250) { session.track(mkMessage(it.toLong()), Direction.Outgoing) }
        session.track(mkMessage(999), Direction.Incoming)

        val removed = session.forget { it.direction == Direction.Outgoing }
        removed shouldBe 250
        val remaining = session.messages()
        remaining.shouldHaveSize(1)
        remaining.single().messageId shouldBe 999
    }

    @Test
    fun forgetAllByDefault() = runBlocking {
        val bot = mkBot()
        val session = bot.sessions!!.get(chatId = 10, userId = 20)
        session.track(mkMessage(1))
        session.track(mkMessage(2))

        session.forget() shouldBe 2
        session.messages() shouldHaveSize 0
    }

    @Test
    fun qualifierIsolatesParallelSessions() = runBlocking {
        val bot = mkBot()
        val mgr = bot.sessions!!
        val wizard = mgr.get(chatId = 10, userId = 20, qualifier = "wizard")
        val support = mgr.get(chatId = 10, userId = 20, qualifier = "support")
        val defaultSession = mgr.get(chatId = 10, userId = 20)

        wizard.track(mkMessage(1))
        support.track(mkMessage(2))
        defaultSession.track(mkMessage(3))

        wizard.messages().map { it.messageId } shouldBe listOf(1L)
        support.messages().map { it.messageId } shouldBe listOf(2L)
        defaultSession.messages().map { it.messageId } shouldBe listOf(3L)
    }
}
