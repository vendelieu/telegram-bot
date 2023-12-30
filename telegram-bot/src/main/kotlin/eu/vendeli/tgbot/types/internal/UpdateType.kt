package eu.vendeli.tgbot.types.internal

enum class UpdateType {
    MESSAGE,
    EDIT_MESSAGE,
    CHANNEL_POST,
    EDITED_CHANNEL_POST,
    MESSAGE_REACTION,
    MESSAGE_REACTION_COUNT,
    INLINE_QUERY,
    CHOSEN_INLINE_RESULT,
    CALLBACK_QUERY,
    SHIPPING_QUERY,
    PRE_CHECKOUT_QUERY,
    POLL,
    POLL_ANSWER,
    MY_CHAT_MEMBER,
    CHAT_MEMBER,
    CHAT_JOIN_REQUEST,
    CHAT_BOOST,
    REMOVED_CHAT_BOOST,
    ;

    override fun toString() = name.lowercase()
}
