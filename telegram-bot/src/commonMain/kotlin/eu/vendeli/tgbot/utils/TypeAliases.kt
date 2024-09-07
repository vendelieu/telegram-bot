package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.interfaces.ctx.ClassManager
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
import eu.vendeli.tgbot.types.internal.CommonMatcher
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
import eu.vendeli.tgbot.types.internal.PurchasedPaidMediaUpdate
import eu.vendeli.tgbot.types.internal.RemovedChatBoostUpdate
import eu.vendeli.tgbot.types.internal.ShippingQueryUpdate
import eu.vendeli.tgbot.types.internal.SingleInputChain
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse

typealias WhenNotHandledActivity = suspend ProcessedUpdate.() -> Unit
typealias OnCommandActivity = suspend CommandContext<ProcessedUpdate>.() -> Unit
typealias OnInputActivity = suspend ActivityCtx<ProcessedUpdate>.() -> Unit

internal typealias OnUpdateActivities = MutableMap<UpdateType, suspend ActivityCtx<ProcessedUpdate>.() -> Unit>
internal typealias InputActivities = MutableMap<String, SingleInputChain>
internal typealias CommandActivities = MutableMap<Pair<String, UpdateType>, FunctionalInvocation>
internal typealias RegexCommandActivities = MutableMap<Regex, FunctionalInvocation>
internal typealias CommonActivities = MutableMap<CommonMatcher, FunctionalInvocation>

typealias HandlingBehaviourBlock = suspend TgUpdateHandler.(ProcessedUpdate) -> Unit
typealias FunctionalHandlingBlock = suspend FunctionalHandlingDsl.() -> Unit
typealias BotConfigurator = BotConfiguration.() -> Unit
typealias RetryStrategy = HttpRequestRetry.ShouldRetryContext.(HttpRequest, HttpResponse) -> Boolean

typealias InvocationLambda = suspend (ClassManager, ProcessedUpdate, User?, TelegramBot, Map<String, String>) -> Any?
typealias Invocable = Pair<InvocationLambda, InvocationMeta>

internal typealias CommandHandlers = Map<Pair<String, UpdateType>, Invocable>
internal typealias InputHandlers = Map<String, Invocable>
internal typealias CommonHandlers = Map<CommonMatcher, Invocable>
internal typealias UpdateTypeHandlers = Map<UpdateType, InvocationLambda>
