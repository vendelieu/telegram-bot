package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.*

sealed class CommandSelector {
    data class String(val command: kotlin.String) : CommandSelector()
    data class Regex(val regex: kotlin.text.Regex) : CommandSelector()

    fun match(command: kotlin.String): Boolean = when (this) {
        is String -> command == this.command
        is Regex -> this.regex.matches(command)
    }
}

data class ManualActions(
    var onMessage: (suspend Message.() -> Unit)? = null,
    var onEditedMessage: (suspend Message.() -> Unit)? = null,
    var onPollAnswer: (suspend PollAnswer.() -> Unit)? = null,
    var onCallbackQuery: (suspend CallbackQuery.() -> Unit)? = null,
    var onPoll: (suspend Poll.() -> Unit)? = null,
    var onChatJoinRequest: (suspend ChatJoinRequest.() -> Unit)? = null,
    var onChatMember: (suspend ChatMemberUpdated.() -> Unit)? = null,
    var onMyChatMember: (suspend ChatMemberUpdated.() -> Unit)? = null,
    var onChannelPost: (suspend Message.() -> Unit)? = null,
    var onEditedChannelPost: (suspend Message.() -> Unit)? = null,
    var onChosenInlineResult: (suspend ChosenInlineResult.() -> Unit)? = null,
    var onInlineQuery: (suspend InlineQuery.() -> Unit)? = null,
    var onPreCheckoutQuery: (suspend PreCheckoutQuery.() -> Unit)? = null,
    var onShippingQuery: (suspend ShippingQuery.() -> Unit)? = null,
    var whenNotHandled: (suspend Update.() -> Unit)? = null,
    var onInput: MutableMap<String, SingleInputChain> = mutableMapOf(),
    var commands: MutableMap<CommandSelector, suspend CommandContext.() -> Unit> = mutableMapOf(),
)
