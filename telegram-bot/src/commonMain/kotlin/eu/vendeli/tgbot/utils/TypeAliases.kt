package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ActivityCtx
import eu.vendeli.tgbot.types.internal.CommandContext
import eu.vendeli.tgbot.types.internal.CommonMatcher
import eu.vendeli.tgbot.types.internal.FunctionalInvocation
import eu.vendeli.tgbot.types.internal.InvocationMeta
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.chain.SingleInputChain
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration
import io.ktor.client.plugins.HttpRetryShouldRetryContext
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse

typealias WhenNotHandledActivity = suspend ProcessedUpdate.() -> Unit
typealias OnCommandActivity = suspend CommandContext<ProcessedUpdate>.() -> Unit
typealias OnInputActivity = suspend ActivityCtx<ProcessedUpdate>.() -> Unit

internal typealias OnUpdateActivities = MutableMap<UpdateType, suspend ActivityCtx<ProcessedUpdate>.() -> Unit>
internal typealias InputActivities = MutableMap<String, SingleInputChain>
internal typealias CommandActivities = MutableMap<Pair<String, UpdateType>, FunctionalInvocation>
internal typealias CommonActivities = MutableMap<CommonMatcher, FunctionalInvocation>

typealias HandlingBehaviourBlock = suspend TgUpdateHandler.(ProcessedUpdate) -> Unit
typealias FunctionalHandlingBlock = suspend FunctionalHandlingDsl.() -> Unit
typealias BotConfigurator = BotConfiguration.() -> Unit
typealias RetryStrategy = HttpRetryShouldRetryContext.(HttpRequest, HttpResponse) -> Boolean

typealias InvocationLambda = suspend (ClassManager, ProcessedUpdate, User?, TelegramBot, Map<String, String>) -> Any?
typealias Invocable = Pair<InvocationLambda, InvocationMeta>

internal typealias CommandHandlers = Map<Pair<String, UpdateType>, Invocable>
internal typealias InputHandlers = Map<String, Invocable>
internal typealias CommonHandlers = Map<CommonMatcher, Invocable>
internal typealias UpdateTypeHandlers = Map<UpdateType, InvocationLambda>
