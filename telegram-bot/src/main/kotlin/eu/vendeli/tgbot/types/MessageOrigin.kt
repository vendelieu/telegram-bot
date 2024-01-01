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
    data class UserOrigin(
        val date: Instant,
        val senderUser: User,
    ) : MessageOrigin("user")

    data class HiddenUser(
        val date: Instant,
        val senderUserName: String,
    ) : MessageOrigin("hidden_user")

    data class ChatOrigin(
        val date: Long,
        val senderChat: Chat,
        val authorSignature: String? = null,
    ) : MessageOrigin("chat")

    data class Channel(
        val date: Instant,
        val chat: Chat,
        val messageId: Long,
        val authorSignature: String? = null,
    ) : MessageOrigin("channel")
}
