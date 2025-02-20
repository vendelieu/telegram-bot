package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import eu.vendeli.tgbot.types.chain.SingleInputChain
import eu.vendeli.tgbot.types.component.ActivityCtx
import eu.vendeli.tgbot.types.component.CommandContext
import eu.vendeli.tgbot.types.component.CommonMatcher
import eu.vendeli.tgbot.types.component.FunctionalInvocation
import eu.vendeli.tgbot.types.component.InvocationMeta
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.configuration.BotConfiguration
import eu.vendeli.tgbot.types.User
import io.ktor.client.plugins.HttpRetryShouldRetryContext
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse

typealias OnCommandActivity = suspend CommandContext<ProcessedUpdate>.() -> Unit
typealias OnInputActivity = suspend ActivityCtx<ProcessedUpdate>.() -> Unit
typealias WhenNotHandledActivity = suspend ActivityCtx<ProcessedUpdate>.() -> Unit

typealias HandlingBehaviourBlock = suspend TgUpdateHandler.(ProcessedUpdate) -> Unit
typealias FunctionalHandlingBlock = suspend FunctionalHandlingDsl.() -> Unit
typealias BotConfigurator = BotConfiguration.() -> Unit
typealias RetryStrategy = HttpRetryShouldRetryContext.(HttpRequest, HttpResponse) -> Boolean

typealias InvocationLambda = suspend (ClassManager, ProcessedUpdate, User?, TelegramBot, Map<String, String>) -> Any?
typealias Invocable = Pair<InvocationLambda, InvocationMeta>

internal typealias OnUpdateActivities = MutableMap<UpdateType, suspend ActivityCtx<ProcessedUpdate>.() -> Unit>
internal typealias InputActivities = MutableMap<String, SingleInputChain>
internal typealias CommandActivities = MutableMap<Pair<String, UpdateType>, FunctionalInvocation>
internal typealias CommonActivities = MutableMap<CommonMatcher, FunctionalInvocation>

internal typealias CommandHandlers = Map<Pair<String, UpdateType>, Invocable>
internal typealias InputHandlers = Map<String, Invocable>
internal typealias CommonHandlers = Map<CommonMatcher, Invocable>
internal typealias UpdateTypeHandlers = Map<UpdateType, InvocationLambda>
