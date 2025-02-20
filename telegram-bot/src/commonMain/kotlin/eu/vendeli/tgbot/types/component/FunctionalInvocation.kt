package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.OnCommandActivity
import kotlin.reflect.KClass

internal data class FunctionalInvocation(
    val id: String,
    val invocation: OnCommandActivity,
    val rateLimits: RateLimits,
    val guard: Guard = DefaultGuard,
    val argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
) {
    override fun toString(): String =
        "FunctionalInvocation[" +
            "id = $id, CommandContext, rateLimits = $rateLimits, guard = $guard, argParser = $argParser]"
}
