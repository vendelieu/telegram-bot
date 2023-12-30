package eu.vendeli.tgbot.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MessageOrigin.User::class, name = "user"),
    JsonSubTypes.Type(value = MessageOrigin.HiddenUser::class, name = "hidden_user"),
    JsonSubTypes.Type(value = MessageOrigin.Chat::class, name = "chat"),
    JsonSubTypes.Type(value = MessageOrigin.Channel::class, name = "channel"),
)
sealed class MessageOrigin(val type: String) {
    data class User(
        val date: Instant,
        val senderUser: eu.vendeli.tgbot.types.User,
    ) : MessageOrigin("user")

    data class HiddenUser(
        val date: Instant,
        val senderUserName: String,
    ) : MessageOrigin("hidden_user")

    data class Chat(
        val date: Long,
        val senderChat: eu.vendeli.tgbot.types.chat.Chat,
        val authorSignature: String? = null,
    ) : MessageOrigin("chat")

    data class Channel(
        val date: Instant,
        val chat: Chat,
        val messageId: Long,
        val authorSignature: String? = null,
    ) : MessageOrigin("channel")
}
