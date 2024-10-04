package eu.vendeli.utils

import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.Response.Success
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.processUpdate
import eu.vendeli.tgbot.utils.serde
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlin.random.Random

@Suppress("ClassName")
sealed class MockUpdate {
    abstract val updates: List<ProcessedUpdate>
    abstract val response: ByteArray

    data class SINGLE(
        val text: String = "/start",
    ) : MockUpdate() {
        override val response: ByteArray
            get() = serde
                .encodeToString(
                    Success(updates),
                ).toByteArray()

        override val updates: List<ProcessedUpdate>
            get() = listOf(Update(Random.nextInt(), generateMsg(text)).processUpdate())
    }

    data class TEXT_LIST(
        val texts: List<String>,
    ) : MockUpdate() {
        override val response: ByteArray
            get() = serde.encodeToString(Success(updates)).toByteArray()

        override val updates: List<ProcessedUpdate>
            get() = texts.map {
                Update(
                    Random.nextInt(),
                    generateMsg(it),
                ).processUpdate()
            }
    }

    data class UPDATES_LIST(
        override val updates: List<ProcessedUpdate>,
    ) : MockUpdate() {
        override val response: ByteArray = serde
            .encodeToString(
                Success(updates),
            ).toByteArray()
    }

    data class RAW_RESPONSE(
        val raw: String,
    ) : MockUpdate() {
        override val response: ByteArray = raw.toByteArray()
        override val updates: List<ProcessedUpdate> = serde.decodeFromString<Success<List<ProcessedUpdate>>>(raw).result
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
