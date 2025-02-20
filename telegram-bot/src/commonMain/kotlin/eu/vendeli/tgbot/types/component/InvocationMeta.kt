package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.fqName
import kotlin.reflect.KClass

data class InvocationMeta(
    val qualifier: String,
    val function: String,
    val rateLimits: RateLimits = RateLimits.NOT_LIMITED,
    val guard: KClass<out Guard> = DefaultGuard::class,
    val argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
) {
    override fun toString(): String = buildString {
        append("$qualifier::$function")
        if (rateLimits.rate > 0 || rateLimits.period > 0) append(" $rateLimits")
        if (guard.fqName != DefaultGuard::class.fqName) append(" | \uD83D\uDEE1\uFE0F ${guard.simpleName}")
        if (argParser.fqName != DefaultArgParser::class.fqName) append(" | \uD83D\uDEE0 ${argParser.simpleName}")
    }
}
