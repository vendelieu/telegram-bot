package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.utils.*

sealed class CommandSelector {
    abstract val rateLimits: RateLimits

    data class String(val command: kotlin.String, override val rateLimits: RateLimits) : CommandSelector()
    data class Regex(val regex: kotlin.text.Regex, override val rateLimits: RateLimits) : CommandSelector()

    fun match(command: kotlin.String): Boolean = when (this) {
        is String -> command == this.command
        is Regex -> this.regex.matches(command)
    }
}

data class ManualActions(
    var onMessage: OnMessageAction? = null,
    var onEditedMessage: OnEditedMessageAction? = null,
    var onPollAnswer: OnPollAnswerAction? = null,
    var onCallbackQuery: OnCallbackQueryAction? = null,
    var onPoll: OnPollAction? = null,
    var onChatJoinRequest: OnChatJoinRequestAction? = null,
    var onChatMember: OnChatMemberAction? = null,
    var onMyChatMember: OnMyChatMemberAction? = null,
    var onChannelPost: OnChannelPostAction? = null,
    var onEditedChannelPost: OnEditedChannelPostAction? = null,
    var onChosenInlineResult: OnChosenInlineResultAction? = null,
    var onInlineQuery: OnInlineQueryAction? = null,
    var onPreCheckoutQuery: OnPreCheckoutQueryAction? = null,
    var onShippingQuery: OnShippingQueryAction? = null,
    var whenNotHandled: WhenNotHandledAction? = null,
    var onInput: InputActions = mutableMapOf(),
    var commands: CommandActions = mutableMapOf(),
)
