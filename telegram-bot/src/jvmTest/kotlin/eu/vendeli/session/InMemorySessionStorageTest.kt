package eu.vendeli.session

import eu.vendeli.tgbot.implementations.InMemorySessionStorage
import eu.vendeli.tgbot.types.component.MessageKind
import eu.vendeli.tgbot.types.session.Direction
import eu.vendeli.tgbot.types.session.SessionKey
import eu.vendeli.tgbot.types.session.TrackedMessage
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class InMemorySessionStorageTest : AnnotationSpec() {
    private val key = SessionKey.ChatUser(chatId = 10, userId = 20)

    private fun entry(id: Long, dir: Direction = Direction.Incoming) = TrackedMessage(
        messageId = id,
        chatId = 10,
        userId = 20,
        kind = MessageKind.TEXT,
        direction = dir,
        businessConnectionId = null,
        at = Clock.System.now(),
    )

    @Test
    fun addAndList() = runBlocking {
        val storage = InMemorySessionStorage()
        storage.add(key, entry(1))
        storage.add(key, entry(2))
        storage.list(key).map { it.messageId }.shouldContainExactly(1, 2)
    }

    @Test
    fun listUnknownKeyReturnsEmpty() = runBlocking {
        val storage = InMemorySessionStorage()
        storage.list(SessionKey.Chat(999)).shouldBeEmpty()
    }

    @Test
    fun removeByPredicate() = runBlocking {
        val storage = InMemorySessionStorage()
        storage.add(key, entry(1, Direction.Incoming))
        storage.add(key, entry(2, Direction.Outgoing))
        storage.add(key, entry(3, Direction.Outgoing))

        val removed = storage.remove(key) { it.direction == Direction.Outgoing }
        removed shouldBe 2
        storage.list(key).map { it.messageId }.shouldContainExactly(1)
    }

    @Test
    fun clearDropsEverythingForKey() = runBlocking {
        val storage = InMemorySessionStorage()
        val other = SessionKey.ChatUser(chatId = 10, userId = 21)
        storage.add(key, entry(1))
        storage.add(other, entry(2))

        storage.clear(key)
        storage.list(key).shouldBeEmpty()
        storage.list(other).shouldHaveSize(1)
    }
}
