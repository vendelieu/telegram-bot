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
    var onMessage: (suspend ActionContext<Message>.() -> Unit)? = null,
    var onEditedMessage: (suspend ActionContext<Message>.() -> Unit)? = null,
    var onPollAnswer: (suspend ActionContext<PollAnswer>.() -> Unit)? = null,
    var onCallbackQuery: (suspend ActionContext<CallbackQuery>.() -> Unit)? = null,
    var onPoll: (suspend ActionContext<Poll>.() -> Unit)? = null,
    var onChatJoinRequest: (suspend ActionContext<ChatJoinRequest>.() -> Unit)? = null,
    var onChatMember: (suspend ActionContext<ChatMemberUpdated>.() -> Unit)? = null,
    var onMyChatMember: (suspend ActionContext<ChatMemberUpdated>.() -> Unit)? = null,
    var onChannelPost: (suspend ActionContext<Message>.() -> Unit)? = null,
    var onEditedChannelPost: (suspend ActionContext<Message>.() -> Unit)? = null,
    var onChosenInlineResult: (suspend ActionContext<ChosenInlineResult>.() -> Unit)? = null,
    var onInlineQuery: (suspend ActionContext<InlineQuery>.() -> Unit)? = null,
    var onPreCheckoutQuery: (suspend ActionContext<PreCheckoutQuery>.() -> Unit)? = null,
    var onShippingQuery: (suspend ActionContext<ShippingQuery>.() -> Unit)? = null,
    var whenNotHandled: (suspend Update.() -> Unit)? = null,
    var onInput: MutableMap<String, SingleInputChain> = mutableMapOf(),
    var commands: MutableMap<CommandSelector, suspend CommandContext.() -> Unit> = mutableMapOf(),
)
