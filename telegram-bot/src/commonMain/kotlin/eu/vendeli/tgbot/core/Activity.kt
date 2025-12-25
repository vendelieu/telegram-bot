package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.configuration.RateLimits
import kotlin.reflect.KClass

/**
 * Represents a unique handler function.
 * ID is computed from: qualifier + function name + parameter types hash.
 */
interface Activity {
    /** Unique identifier (hash of signature). */
    val id: Int

    /** Qualified class name. */
    val qualifier: String

    /** Function name. */
    val function: String

    /** Rate limits, unlimited if not set. */
    val rateLimits: RateLimits
        get() = RateLimits.NOT_LIMITED

    /** Guard class, [eu.vendeli.tgbot.implementations.DefaultGuard] if not set. */
    val guardClass: KClass<out Guard>
        get() = DefaultGuard::class

    /** Argument parser class, [eu.vendeli.tgbot.implementations.DefaultArgParser] if not set. */
    val argParser: KClass<out ArgumentParser>
        get() = DefaultArgParser::class

    /** Invoke the handler. */
    suspend fun invoke(context: ProcessingContext): Any?
}
