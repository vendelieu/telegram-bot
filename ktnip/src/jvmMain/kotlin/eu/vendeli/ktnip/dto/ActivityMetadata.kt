package eu.vendeli.ktnip.dto

import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.fqName

/**
 * Represents metadata for an Activity before code generation.
 * Consolidates all properties needed to generate an Activity object.
 */
data class ActivityMetadata(
    /** Unique identifier (hash of function signature). */
    val id: Int,
    /** Qualified class name. */
    val qualifier: String,
    /** Function name. */
    val function: String,
    /** Rate limits configuration. */
    val rateLimits: RateLimits = RateLimits.NOT_LIMITED,
    /** Fully qualified name of Guard class. */
    val guardClass: String = DefaultGuard::class.fqName,
    /** Fully qualified name of ArgumentParser class. */
    val argParserClass: String = DefaultArgParser::class.fqName,
)
