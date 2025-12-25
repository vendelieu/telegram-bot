package eu.vendeli.ksp.collectors

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import eu.vendeli.ksp.annotation.CommonHandlerData
import eu.vendeli.ksp.dto.CommonAnnotationData
import eu.vendeli.ksp.dto.CommonAnnotationValue
import eu.vendeli.ksp.utils.cast

/**
 * Immutable registry for common handlers (text/regex matchers).
 * Replaces the mutable singleton pattern with a builder approach.
 */
class CommonHandlerRegistry private constructor(
    private val handlers: List<CommonAnnotationData>,
) {
    /**
     * Builder for CommonHandlerRegistry.
     */
    class Builder {
        private val handlers = mutableListOf<CommonAnnotationData>()

        /**
         * Registers a common handler from function and annotation data.
         *
         * @param function The function declaration
         * @param parsedData Parsed annotation data
         * @param rateLimits Rate limits from function
         * @param argParser Argument parser from function
         */
        fun register(
            function: KSFunctionDeclaration,
            parsedData: CommonHandlerData,
            rateLimits: eu.vendeli.tgbot.types.configuration.RateLimits,
            argParser: String,
        ) {
            val qualifier = function.qualifiedName!!.getQualifier()
            val simpleName = function.simpleName.asString()

            when (val value = parsedData.value) {
                is List<*> -> {
                    value.forEach { item ->
                        handlers.add(
                            CommonAnnotationData(
                                funQualifier = qualifier,
                                funSimpleName = simpleName,
                                value = CommonAnnotationValue.String(item.cast()),
                                filter = parsedData.filter,
                                argParser = argParser,
                                priority = parsedData.priority,
                                rateLimits = rateLimits,
                                scope = parsedData.scope,
                                funDeclaration = function,
                            ),
                        )
                    }
                }
                is String -> {
                    handlers.add(
                        CommonAnnotationData(
                            funQualifier = qualifier,
                            funSimpleName = simpleName,
                            value = CommonAnnotationValue.Regex(value.toRegex(parsedData.regexOptions.toSet())),
                            filter = parsedData.filter,
                            argParser = argParser,
                            priority = parsedData.priority,
                            rateLimits = rateLimits,
                            scope = parsedData.scope,
                            funDeclaration = function,
                        ),
                    )
                }
            }
        }

        /**
         * Builds the immutable registry.
         */
        fun build(): CommonHandlerRegistry =
            CommonHandlerRegistry(handlers.sortedBy { it.priority })
    }

    /**
     * Gets all registered handlers, sorted by priority.
     */
    fun getAll(): List<CommonAnnotationData> = handlers

    companion object {
        /**
         * Creates a builder for constructing the registry.
         */
        fun builder(): Builder = Builder()
    }
}
