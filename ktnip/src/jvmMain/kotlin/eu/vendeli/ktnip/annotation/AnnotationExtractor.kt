package eu.vendeli.ktnip.annotation

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import eu.vendeli.ktnip.utils.findAnnotationRecursively
import eu.vendeli.tgbot.annotations.ArgParser
import eu.vendeli.tgbot.annotations.Guard
import eu.vendeli.tgbot.annotations.RateLimits
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.types.configuration.RateLimits as RateLimitsConfig

/**
 * Extracts annotations from functions with priority: function > class > default.
 * Centralizes annotation extraction logic that was duplicated in collectors.
 */
object AnnotationExtractor {
    /**
     * Extracts Guard annotation with priority: function > class > default.
     *
     * @param function The function to extract from
     * @return Fully qualified name of the Guard class
     */
    fun extractGuard(function: KSFunctionDeclaration): String =
        function.annotations
            .findAnnotationRecursively(Guard::class)
            ?.arguments
            ?.let { AnnotationParser.parseGuard(it) }
            ?: function.closestClassDeclaration()
                ?.annotations
                ?.findAnnotationRecursively(Guard::class)
                ?.arguments
                ?.let { AnnotationParser.parseGuard(it) }
            ?: DefaultGuard::class.qualifiedName!!

    /**
     * Extracts RateLimits annotation with priority: function > class > default.
     *
     * @param function The function to extract from
     * @return RateLimits configuration
     */
    fun extractRateLimits(function: KSFunctionDeclaration): RateLimitsConfig =
        function.annotations
            .findAnnotationRecursively(RateLimits::class)
            ?.arguments
            ?.let { AnnotationParser.parseRateLimits(it) }
            ?: function.closestClassDeclaration()
                ?.annotations
                ?.findAnnotationRecursively(RateLimits::class)
                ?.arguments
                ?.let { AnnotationParser.parseRateLimits(it) }
            ?: RateLimitsConfig(0, 0)

    /**
     * Extracts ArgParser annotation with priority: function > class > default.
     *
     * @param function The function to extract from
     * @return Fully qualified name of the ArgumentParser class
     */
    fun extractArgParser(function: KSFunctionDeclaration): String =
        function.annotations
            .findAnnotationRecursively(ArgParser::class)
            ?.arguments
            ?.let { AnnotationParser.parseArgParser(it) }
            ?: function.closestClassDeclaration()
                ?.annotations
                ?.findAnnotationRecursively(ArgParser::class)
                ?.arguments
                ?.let { AnnotationParser.parseArgParser(it) }
            ?: DefaultArgParser::class.qualifiedName!!
}
