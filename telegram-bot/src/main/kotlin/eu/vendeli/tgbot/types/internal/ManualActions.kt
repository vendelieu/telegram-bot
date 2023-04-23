package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.CommandActions
import eu.vendeli.tgbot.utils.InputActions
import eu.vendeli.tgbot.utils.OnCallbackQueryAction
import eu.vendeli.tgbot.utils.OnChannelPostAction
import eu.vendeli.tgbot.utils.OnChatJoinRequestAction
import eu.vendeli.tgbot.utils.OnChatMemberAction
import eu.vendeli.tgbot.utils.OnChosenInlineResultAction
import eu.vendeli.tgbot.utils.OnEditedChannelPostAction
import eu.vendeli.tgbot.utils.OnEditedMessageAction
import eu.vendeli.tgbot.utils.OnInlineQueryAction
import eu.vendeli.tgbot.utils.OnMessageAction
import eu.vendeli.tgbot.utils.OnMyChatMemberAction
import eu.vendeli.tgbot.utils.OnPollAction
import eu.vendeli.tgbot.utils.OnPollAnswerAction
import eu.vendeli.tgbot.utils.OnPreCheckoutQueryAction
import eu.vendeli.tgbot.utils.OnShippingQueryAction
import eu.vendeli.tgbot.utils.WhenNotHandledAction

internal sealed class CommandSelector(open val scope: Set<CommandScope>, open val rateLimits: RateLimits) {
    data class String(
        val command: kotlin.String,
        override val scope: Set<CommandScope>,
        override val rateLimits: RateLimits,
    ) : CommandSelector(scope, rateLimits)

    data class Regex(
        val regex: kotlin.text.Regex,
        override val scope: Set<CommandScope>,
        override val rateLimits: RateLimits,
    ) : CommandSelector(scope, rateLimits)

    fun match(command: kotlin.String, scope: CommandScope): Boolean = when (this) {
        is String -> scope in this.scope && command == this.command
        is Regex -> scope in this.scope && regex.matches(command)
    }
}

internal data class ManualActions(
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
