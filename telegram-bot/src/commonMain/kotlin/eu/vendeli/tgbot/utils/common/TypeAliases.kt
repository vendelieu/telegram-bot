package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.types.chain.SingleInputChain
import eu.vendeli.tgbot.types.component.ActivityCtx
import eu.vendeli.tgbot.types.component.CommandContext
import eu.vendeli.tgbot.types.component.CommonMatcher
import eu.vendeli.tgbot.types.component.FunctionalInvocation
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.configuration.BotConfiguration
import io.ktor.client.plugins.HttpRetryShouldRetryContext
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse

import eu.vendeli.tgbot.types.component.ProcessingContext

private typealias DefaultActivityCtx = suspend ActivityCtx<ProcessedUpdate>.() -> Unit

typealias OnCommandActivity = suspend CommandContext<ProcessedUpdate>.() -> Unit
typealias OnInputActivity = DefaultActivityCtx
typealias WhenNotHandledActivity = DefaultActivityCtx

typealias HandlingBehaviourBlock = suspend TgUpdateHandler.(ProcessedUpdate) -> Unit
typealias FunctionalHandlingBlock = suspend FunctionalHandlingDsl.() -> Unit
typealias BotConfigurator = BotConfiguration.() -> Unit
typealias RetryStrategy = HttpRetryShouldRetryContext.(HttpRequest, HttpResponse) -> Boolean

typealias InvocationLambda = suspend (ProcessingContext) -> Any?

internal typealias OnUpdateActivities = MutableMap<UpdateType, DefaultActivityCtx>
internal typealias InputActivities = MutableMap<String, SingleInputChain>
internal typealias CommandActivities = MutableMap<Pair<String, UpdateType>, FunctionalInvocation>
internal typealias CommonActivities = MutableMap<CommonMatcher, FunctionalInvocation>
