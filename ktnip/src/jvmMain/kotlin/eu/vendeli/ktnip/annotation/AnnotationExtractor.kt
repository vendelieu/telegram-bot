package eu.vendeli.ktnip.annotation

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.symbol.KSClassDeclaration
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
            ?: function
                .closestClassDeclaration()
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
            ?: function
                .closestClassDeclaration()
                ?.annotations
                ?.findAnnotationRecursively(RateLimits::class)
                ?.arguments
                ?.let { AnnotationParser.parseRateLimits(it) }
            ?: RateLimitsConfig.NOT_LIMITED

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
            ?: function
                .closestClassDeclaration()
                ?.annotations
                ?.findAnnotationRecursively(ArgParser::class)
                ?.arguments
                ?.let { AnnotationParser.parseArgParser(it) }
            ?: DefaultArgParser::class.qualifiedName!!

    /**
     * Extracts Guard annotation from a class declaration.
     *
     * @param classDecl The class to extract from
     * @return Fully qualified name of the Guard class
     */
    fun extractGuard(classDecl: KSClassDeclaration): String? =
        classDecl.annotations
            .findAnnotationRecursively(Guard::class)
            ?.arguments
            ?.let { AnnotationParser.parseGuard(it) }

    /**
     * Extracts RateLimits annotation from a class declaration.
     *
     * @param classDecl The class to extract from
     * @return RateLimits configuration
     */
    fun extractRateLimits(classDecl: KSClassDeclaration): RateLimitsConfig =
        classDecl.annotations
            .findAnnotationRecursively(RateLimits::class)
            ?.arguments
            ?.let { AnnotationParser.parseRateLimits(it) }
            ?: RateLimitsConfig.NOT_LIMITED

    /**
     * Extracts ArgParser annotation from a class declaration.
     *
     * @param classDecl The class to extract from
     * @return Fully qualified name of the ArgumentParser class
     */
    fun extractArgParser(classDecl: KSClassDeclaration): String? =
        classDecl.annotations
            .findAnnotationRecursively(ArgParser::class)
            ?.arguments
            ?.let { AnnotationParser.parseArgParser(it) }
}
