package eu.vendeli.utils

import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.Response.Success
import eu.vendeli.tgbot.utils.serde
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlin.random.Random

@Suppress("ClassName")
sealed class MockUpdate {
    abstract val response: ByteArray

    data class SINGLE(val text: String = "/start") : MockUpdate() {
        override val response: ByteArray
            get() = serde.encodeToString(
                Success(
                    listOf(Update(Random.nextInt(), generateMsg(text))),
                ),
            ).toByteArray()
    }

    data class TEXT_LIST(val texts: List<String>) : MockUpdate() {
        override val response: ByteArray
            get() = serde.encodeToString(
                Success(
                    texts.map { Update(Random.nextInt(), generateMsg(it)) },
                ),
            ).toByteArray()
    }

    data class UPDATES_LIST(val updates: List<Update>) : MockUpdate() {
        override val response: ByteArray = serde.encodeToString(
            Success(updates),
        ).toByteArray()
    }

    data class RAW_RESPONSE(val raw: String) : MockUpdate() {
        override val response: ByteArray = raw.toByteArray()
    }

    protected fun generateMsg(text: String) = run {
        Message(
            Random.nextLong(),
            from = User(1, false, "Test"),
            chat = Chat(1, ChatType.Private),
            date = Instant.DISTANT_PAST,
            text = text,
        )
    }
}
