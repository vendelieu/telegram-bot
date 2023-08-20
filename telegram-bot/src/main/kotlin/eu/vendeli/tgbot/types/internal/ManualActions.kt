package eu.vendeli.tgbot.types.internal

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
import eu.vendeli.tgbot.utils.RegexCommandActions
import eu.vendeli.tgbot.utils.WhenNotHandledAction

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
    var regexCommands: RegexCommandActions = mutableMapOf(),
)
