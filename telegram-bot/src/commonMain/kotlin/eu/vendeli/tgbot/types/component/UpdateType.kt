package eu.vendeli.tgbot.types.component

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UpdateType {
    @SerialName("message")
    MESSAGE,

    @SerialName("edited_message")
    EDITED_MESSAGE,

    @SerialName("channel_post")
    CHANNEL_POST,

    @SerialName("edited_channel_post")
    EDITED_CHANNEL_POST,

    @SerialName("business_connection")
    BUSINESS_CONNECTION,

    @SerialName("business_message")
    BUSINESS_MESSAGE,

    @SerialName("edited_business_message")
    EDITED_BUSINESS_MESSAGE,

    @SerialName("deleted_business_messages")
    DELETED_BUSINESS_MESSAGES,

    @SerialName("message_reaction")
    MESSAGE_REACTION,

    @SerialName("message_reaction_count")
    MESSAGE_REACTION_COUNT,

    @SerialName("inline_query")
    INLINE_QUERY,

    @SerialName("chosen_inline_result")
    CHOSEN_INLINE_RESULT,

    @SerialName("callback_query")
    CALLBACK_QUERY,

    @SerialName("shipping_query")
    SHIPPING_QUERY,

    @SerialName("pre_checkout_query")
    PRE_CHECKOUT_QUERY,

    @SerialName("poll")
    POLL,

    @SerialName("poll_answer")
    POLL_ANSWER,

    @SerialName("my_chat_member")
    MY_CHAT_MEMBER,

    @SerialName("chat_member")
    CHAT_MEMBER,

    @SerialName("chat_join_request")
    CHAT_JOIN_REQUEST,

    @SerialName("chat_boost")
    CHAT_BOOST,

    @SerialName("removed_chat_boost")
    REMOVED_CHAT_BOOST,

    @SerialName("purchased_paid_media")
    PURCHASED_PAID_MEDIA,

    ;

    override fun toString(): String = "UpdateType.$name"
}
