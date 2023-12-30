package eu.vendeli.utils

import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.Response.Success
import java.time.Instant
import kotlin.random.Random

@Suppress("ClassName")
sealed class MockUpdate {
    abstract val response: ByteArray

    data class SINGLE(val text: String = "/start") : MockUpdate() {
        override val response: ByteArray
            get() = mapper.writeValueAsBytes(
                Success(
                    listOf(Update(Random.nextInt(), generateMsg(text))),
                ),
            )
    }

    data class TEXT_LIST(val texts: List<String>) : MockUpdate() {
        override val response: ByteArray
            get() = mapper.writeValueAsBytes(
                Success(
                    texts.map { Update(Random.nextInt(), generateMsg(it)) },
                ),
            )
    }

    data class UPDATES_LIST(val updates: List<Update>) : MockUpdate() {
        override val response: ByteArray = mapper.writeValueAsBytes(
            Success(updates),
        )
    }

    data class RAW_RESPONSE(val raw: String) : MockUpdate() {
        override val response: ByteArray = raw.toByteArray()
    }

    protected fun generateMsg(text: String) = run {
        Message(
            Random.nextLong(),
            from = User(1, false, "Test"),
            chat = Chat(1, ChatType.Private),
            date = Instant.EPOCH,
            text = text,
        )
    }
}
