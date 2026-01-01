package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.types.component.ActivityCtx
import eu.vendeli.tgbot.types.component.CommandContext
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.configuration.BotConfiguration
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

private typealias DefaultActivityCtx = suspend ActivityCtx<ProcessedUpdate>.() -> Unit

typealias OnCommandActivity = suspend CommandContext<ProcessedUpdate>.() -> Unit
typealias OnInputActivity = DefaultActivityCtx
typealias WhenNotHandledActivity = DefaultActivityCtx

typealias HandlingBehaviourBlock = suspend TgUpdateHandler.(ProcessedUpdate) -> Unit
typealias FunctionalHandlingBlock = suspend FunctionalHandlingDsl.() -> Unit
typealias BotConfigurator = BotConfiguration.() -> Unit
typealias RetryStrategy = HttpRetryShouldRetryContext.(HttpRequest, HttpResponse) -> Boolean
