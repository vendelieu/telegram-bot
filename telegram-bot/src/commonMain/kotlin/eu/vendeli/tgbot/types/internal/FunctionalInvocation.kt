package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.OnCommandActivity
import kotlin.reflect.KClass

internal data class FunctionalInvocation(
    val id: String,
    val invocation: OnCommandActivity,
    val scope: Set<UpdateType>,
    val rateLimits: RateLimits,
    val guard: KClass<out Guard> = DefaultGuard::class,
    val filter: KClass<out Filter> = DefaultFilter::class,
    val argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
) {
    override fun toString(): String =
        "FunctionalInvocation[" +
            "id = $id, CommandContext, scope = $scope, rateLimits = $rateLimits, guard = $guard, filter = $filter" +
            ", argParser = $argParser]"
}
