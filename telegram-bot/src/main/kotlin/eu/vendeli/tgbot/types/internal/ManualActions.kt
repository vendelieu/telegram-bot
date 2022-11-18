package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.utils.*

sealed class CommandSelector {
    data class String(val command: kotlin.String) : CommandSelector()
    data class Regex(val regex: kotlin.text.Regex) : CommandSelector()

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
