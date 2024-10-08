package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the origin of a message. It can be one of
 * - MessageOriginUser
 * - MessageOriginHiddenUser
 * - MessageOriginChat
 * - MessageOriginChannel
 *
 * [Api reference](https://core.telegram.org/bots/api#messageorigin)
 *
 */
@Serializable
sealed class MessageOrigin(
    val type: String,
) {
    @Serializable(InstantSerializer::class)
    abstract val date: Instant
    open val from: User?
        get() = null
    open val fromChat: Chat?
        get() = null
    open val fromMessageId: Long?
        get() = null
    open val signature: String?
        get() = null
    open val senderName: String?
        get() = null

    @Serializable
    @SerialName("user")
    @TgAPI.Name("MessageOriginUser")
    data class UserOrigin(
        @Serializable(InstantSerializer::class)
        override val date: Instant,
        val senderUser: User,
    ) : MessageOrigin("user") {
        override val from: User
            get() = senderUser
    }

    @Serializable
    @SerialName("hidden_user")
    data class HiddenUser(
        @Serializable(InstantSerializer::class)
        override val date: Instant,
        val senderUserName: String,
    ) : MessageOrigin("hidden_user") {
        override val senderName: String
            get() = senderUserName
    }

    @Serializable
    @SerialName("chat")
    @TgAPI.Name("MessageOriginChat")
    data class ChatOrigin(
        @Serializable(InstantSerializer::class)
        override val date: Instant,
        val senderChat: Chat,
        val authorSignature: String? = null,
    ) : MessageOrigin("chat") {
        override val fromChat: Chat
            get() = senderChat
        override val signature: String?
            get() = authorSignature
    }

    @Serializable
    @SerialName("channel")
    data class Channel(
        @Serializable(InstantSerializer::class)
        override val date: Instant,
        val chat: Chat,
        val messageId: Long,
        val authorSignature: String? = null,
    ) : MessageOrigin("channel") {
        override val fromChat: Chat
            get() = chat
        override val fromMessageId: Long
            get() = messageId
        override val signature: String?
            get() = authorSignature
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun asUserOrigin() = this as? UserOrigin

    @Suppress("NOTHING_TO_INLINE")
    inline fun asHiddenUser() = this as? HiddenUser

    @Suppress("NOTHING_TO_INLINE")
    inline fun asChatOrigin() = this as? ChatOrigin

    @Suppress("NOTHING_TO_INLINE")
    inline fun asChannel() = this as? Channel
}
