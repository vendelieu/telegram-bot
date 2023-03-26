package eu.vendeli.tgbot.types.internal

enum class UpdateType {
    MESSAGE, EDIT_MESSAGE, CHANNEL_POST, EDITED_CHANNEL_POST, INLINE_QUERY, CHOSEN_INLINE_RESULT, CALLBACK_QUERY,
    SHIPPING_QUERY, PRE_CHECKOUT_QUERY, POLL, POLL_ANSWER, MY_CHAT_MEMBER, CHAT_MEMBER, CHAT_JOIN_REQUEST;

    internal val scope: CommandScope?
        get() = when (this) {
            MESSAGE -> CommandScope.MESSAGE
            CALLBACK_QUERY -> CommandScope.CALLBACK
            else -> null
        }
}
