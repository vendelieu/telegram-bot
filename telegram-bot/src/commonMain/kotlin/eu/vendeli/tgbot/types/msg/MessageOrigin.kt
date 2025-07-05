package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

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
sealed class MessageOrigin {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable(InstantSerializer::class)
    abstract val date: Instant

    @TgAPI.Ignore
    open val from: User?
        get() = null

    @TgAPI.Ignore
    open val fromChat: Chat?
        get() = null

    @TgAPI.Ignore
    open val fromMessageId: Long?
        get() = null

    @TgAPI.Ignore
    open val signature: String?
        get() = null

    @TgAPI.Ignore
    open val senderName: String?
        get() = null

    @Serializable
    @SerialName("user")
    @TgAPI.Name("MessageOriginUser")
    data class UserOrigin(
        @Serializable(InstantSerializer::class)
        override val date: Instant,
        val senderUser: User,
    ) : MessageOrigin() {
        override val from: User
            get() = senderUser
    }

    @Serializable
    @SerialName("hidden_user")
    data class HiddenUser(
        @Serializable(InstantSerializer::class)
        override val date: Instant,
        val senderUserName: String,
    ) : MessageOrigin() {
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
    ) : MessageOrigin() {
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
    ) : MessageOrigin() {
        override val fromChat: Chat
            get() = chat
        override val fromMessageId: Long
            get() = messageId
        override val signature: String?
            get() = authorSignature
    }

    inline fun asUserOrigin() = this as? UserOrigin

    inline fun asHiddenUser() = this as? HiddenUser

    inline fun asChatOrigin() = this as? ChatOrigin

    inline fun asChannel() = this as? Channel
}
