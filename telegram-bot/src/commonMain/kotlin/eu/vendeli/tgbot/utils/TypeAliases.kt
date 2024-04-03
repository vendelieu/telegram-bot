package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ActivityCtx
import eu.vendeli.tgbot.types.internal.BusinessConnectionUpdate
import eu.vendeli.tgbot.types.internal.BusinessMessageUpdate
import eu.vendeli.tgbot.types.internal.CallbackQueryUpdate
import eu.vendeli.tgbot.types.internal.ChannelPostUpdate
import eu.vendeli.tgbot.types.internal.ChatBoostUpdate
import eu.vendeli.tgbot.types.internal.ChatJoinRequestUpdate
import eu.vendeli.tgbot.types.internal.ChatMemberUpdate
import eu.vendeli.tgbot.types.internal.ChosenInlineResultUpdate
import eu.vendeli.tgbot.types.internal.CommandContext
import eu.vendeli.tgbot.types.internal.DeletedBusinessMessagesUpdate
import eu.vendeli.tgbot.types.internal.EditedBusinessMessageUpdate
import eu.vendeli.tgbot.types.internal.EditedChannelPostUpdate
import eu.vendeli.tgbot.types.internal.EditedMessageUpdate
import eu.vendeli.tgbot.types.internal.FunctionalInvocation
import eu.vendeli.tgbot.types.internal.InlineQueryUpdate
import eu.vendeli.tgbot.types.internal.InvocationMeta
import eu.vendeli.tgbot.types.internal.MessageReactionCountUpdate
import eu.vendeli.tgbot.types.internal.MessageReactionUpdate
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.MyChatMemberUpdate
import eu.vendeli.tgbot.types.internal.PollAnswerUpdate
import eu.vendeli.tgbot.types.internal.PollUpdate
import eu.vendeli.tgbot.types.internal.PreCheckoutQueryUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.RemovedChatBoostUpdate
import eu.vendeli.tgbot.types.internal.ShippingQueryUpdate
import eu.vendeli.tgbot.types.internal.SingleInputChain
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse

typealias OnMessageActivity = suspend ActivityCtx<MessageUpdate>.() -> Unit
typealias OnEditedMessageActivity = suspend ActivityCtx<EditedMessageUpdate>.() -> Unit
typealias OnPollAnswerActivity = suspend ActivityCtx<PollAnswerUpdate>.() -> Unit
typealias OnCallbackQueryActivity = suspend ActivityCtx<CallbackQueryUpdate>.() -> Unit
typealias OnPollActivity = suspend ActivityCtx<PollUpdate>.() -> Unit
typealias OnChatJoinRequestActivity = suspend ActivityCtx<ChatJoinRequestUpdate>.() -> Unit
typealias OnChatMemberActivity = suspend ActivityCtx<ChatMemberUpdate>.() -> Unit
typealias OnMyChatMemberActivity = suspend ActivityCtx<MyChatMemberUpdate>.() -> Unit
typealias OnChannelPostActivity = suspend ActivityCtx<ChannelPostUpdate>.() -> Unit
typealias OnEditedChannelPostActivity = suspend ActivityCtx<EditedChannelPostUpdate>.() -> Unit
typealias OnChosenInlineResultActivity = suspend ActivityCtx<ChosenInlineResultUpdate>.() -> Unit
typealias OnInlineQueryActivity = suspend ActivityCtx<InlineQueryUpdate>.() -> Unit
typealias OnPreCheckoutQueryActivity = suspend ActivityCtx<PreCheckoutQueryUpdate>.() -> Unit
typealias OnShippingQueryActivity = suspend ActivityCtx<ShippingQueryUpdate>.() -> Unit
typealias OnMessageReactionActivity = suspend ActivityCtx<MessageReactionUpdate>.() -> Unit
typealias OnMessageReactionCountActivity = suspend ActivityCtx<MessageReactionCountUpdate>.() -> Unit
typealias OnChatBoostActivity = suspend ActivityCtx<ChatBoostUpdate>.() -> Unit
typealias OnRemovedChatBoostActivity = suspend ActivityCtx<RemovedChatBoostUpdate>.() -> Unit
typealias OnBusinessConnectionActivity = suspend ActivityCtx<BusinessConnectionUpdate>.() -> Unit
typealias OnBusinessMessageActivity = suspend ActivityCtx<BusinessMessageUpdate>.() -> Unit
typealias OnEditedBusinessMessageActivity = suspend ActivityCtx<EditedBusinessMessageUpdate>.() -> Unit
typealias OnDeletedBusinessMessagesActivity = suspend ActivityCtx<DeletedBusinessMessagesUpdate>.() -> Unit

typealias WhenNotHandledActivity = suspend Update.() -> Unit
typealias OnCommandActivity = suspend CommandContext<ProcessedUpdate>.() -> Unit
typealias OnInputActivity = suspend ActivityCtx<ProcessedUpdate>.() -> Unit

typealias OnUpdateActivities = MutableMap<UpdateType, suspend ActivityCtx<ProcessedUpdate>.() -> Unit>
typealias InputActivities = MutableMap<String, SingleInputChain>
internal typealias CommandActivities = MutableMap<Pair<String, UpdateType>, FunctionalInvocation>
internal typealias RegexCommandActivities = MutableMap<Regex, FunctionalInvocation>

typealias HandlingBehaviourBlock = suspend TgUpdateHandler.(Update) -> Unit
typealias FunctionalHandlingBlock = suspend FunctionalHandlingDsl.() -> Unit
typealias BotConfigurator = BotConfiguration.() -> Unit
typealias RetryStrategy = HttpRequestRetry.ShouldRetryContext.(HttpRequest, HttpResponse) -> Boolean

typealias InvocationLambda = suspend (ClassManager, ProcessedUpdate, User?, TelegramBot, Map<String, String>) -> Any?
typealias Invocable = Pair<InvocationLambda, InvocationMeta>

internal typealias CommandHandlers = Map<Pair<String, UpdateType>, Invocable>
internal typealias InputHandlers = Map<String, Invocable>
internal typealias RegexHandlers = Map<Regex, Invocable>
internal typealias UpdateTypeHandlers = Map<UpdateType, InvocationLambda>
