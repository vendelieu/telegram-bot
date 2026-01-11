package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.configuration.RateLimits
import kotlin.reflect.KClass

/**
 * Activity created at runtime via functional DSL.
 * Implements the same interface as KSP-generated activities.
 */
class LambdaActivity(
    override val id: Int,
    override val qualifier: String = "functional",
    override val function: String,
    override val rateLimits: RateLimits = RateLimits.NOT_LIMITED,
    override val guardClass: KClass<out Guard> = DefaultGuard::class,
    override val argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
    private val action: suspend ProcessingContext.() -> Any?,
) : Activity {
    override suspend fun invoke(context: ProcessingContext): Any? = action(context)
}
