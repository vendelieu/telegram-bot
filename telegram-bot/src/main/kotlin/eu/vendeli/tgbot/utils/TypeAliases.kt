package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.core.ManualHandlingDsl
import eu.vendeli.tgbot.core.TelegramUpdateHandler
import eu.vendeli.tgbot.types.CallbackQuery
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.Poll
import eu.vendeli.tgbot.types.PollAnswer
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.chat.ChatJoinRequest
import eu.vendeli.tgbot.types.chat.ChatMemberUpdated
import eu.vendeli.tgbot.types.inline.ChosenInlineResult
import eu.vendeli.tgbot.types.inline.InlineQuery
import eu.vendeli.tgbot.types.internal.ActionContext
import eu.vendeli.tgbot.types.internal.CommandContext
import eu.vendeli.tgbot.types.internal.InputContext
import eu.vendeli.tgbot.types.internal.ManualInvocation
import eu.vendeli.tgbot.types.internal.SingleInputChain
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration
import eu.vendeli.tgbot.types.payment.PreCheckoutQuery
import eu.vendeli.tgbot.types.payment.ShippingQuery

typealias OnMessageAction = suspend ActionContext<Message>.() -> Unit
typealias OnEditedMessageAction = suspend ActionContext<Message>.() -> Unit
typealias OnPollAnswerAction = suspend ActionContext<PollAnswer>.() -> Unit
typealias OnCallbackQueryAction = suspend ActionContext<CallbackQuery>.() -> Unit
typealias OnPollAction = suspend ActionContext<Poll>.() -> Unit
typealias OnChatJoinRequestAction = suspend ActionContext<ChatJoinRequest>.() -> Unit
typealias OnChatMemberAction = suspend ActionContext<ChatMemberUpdated>.() -> Unit
typealias OnMyChatMemberAction = suspend ActionContext<ChatMemberUpdated>.() -> Unit
typealias OnChannelPostAction = suspend ActionContext<Message>.() -> Unit
typealias OnEditedChannelPostAction = suspend ActionContext<Message>.() -> Unit
typealias OnChosenInlineResultAction = suspend ActionContext<ChosenInlineResult>.() -> Unit
typealias OnInlineQueryAction = suspend ActionContext<InlineQuery>.() -> Unit
typealias OnPreCheckoutQueryAction = suspend ActionContext<PreCheckoutQuery>.() -> Unit
typealias OnShippingQueryAction = suspend ActionContext<ShippingQuery>.() -> Unit
typealias WhenNotHandledAction = suspend Update.() -> Unit
typealias OnCommandAction = suspend CommandContext.() -> Unit
typealias OnInputAction = suspend InputContext.() -> Unit

typealias InputActions = MutableMap<String, SingleInputChain>
internal typealias CommandActions = MutableMap<String, ManualInvocation>
internal typealias RegexCommandActions = MutableMap<Regex, ManualInvocation>

typealias HandlingBehaviourBlock = suspend TelegramUpdateHandler.(Update) -> Unit
typealias ManualHandlingBlock = suspend ManualHandlingDsl.() -> Unit
typealias BotConfigurator = BotConfiguration.() -> Unit

