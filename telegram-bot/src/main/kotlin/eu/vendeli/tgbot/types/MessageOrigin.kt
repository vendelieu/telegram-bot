package eu.vendeli.tgbot.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import eu.vendeli.tgbot.types.chat.Chat
import java.time.Instant

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MessageOrigin.UserOrigin::class, name = "user"),
    JsonSubTypes.Type(value = MessageOrigin.HiddenUser::class, name = "hidden_user"),
    JsonSubTypes.Type(value = MessageOrigin.ChatOrigin::class, name = "chat"),
    JsonSubTypes.Type(value = MessageOrigin.Channel::class, name = "channel"),
)
sealed class MessageOrigin(val type: String) {
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

    data class UserOrigin(
        override val date: Instant,
        val senderUser: User,
    ) : MessageOrigin("user") {
        override val from: User
            get() = senderUser
    }

    data class HiddenUser(
        override val date: Instant,
        val senderUserName: String,
    ) : MessageOrigin("hidden_user") {
        override val senderName: String
            get() = senderUserName
    }

    data class ChatOrigin(
        override val date: Instant,
        val senderChat: Chat,
        val authorSignature: String? = null,
    ) : MessageOrigin("chat") {
        override val fromChat: Chat
            get() = senderChat
        override val signature: String?
            get() = authorSignature
    }

    data class Channel(
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
