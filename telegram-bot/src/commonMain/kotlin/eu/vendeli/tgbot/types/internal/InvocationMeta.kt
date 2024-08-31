package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.fullName
import kotlin.reflect.KClass

data class InvocationMeta(
    val qualifier: String,
    val function: String,
    val rateLimits: RateLimits,
    val guard: KClass<out Guard> = DefaultGuard::class,
    val argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
) {
    override fun toString(): String = buildString {
        append("$qualifier::$function $rateLimits")
        if (guard.fullName != DefaultGuard::class.fullName) append(" | \uD83D\uDEE1\uFE0F ${guard.simpleName}")
        if (argParser.fullName != DefaultArgParser::class.fullName) append(" | \uD83D\uDEE0 ${argParser.simpleName}")
    }
}
