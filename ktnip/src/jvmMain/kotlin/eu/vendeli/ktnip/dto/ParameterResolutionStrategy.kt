package eu.vendeli.ktnip.dto

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

/**
 * Represents different strategies for resolving function parameters during Activity invocation.
 * Each strategy knows how to generate code for obtaining its value.
 */
sealed class ParameterResolutionStrategy {
    abstract val typeName: TypeName
    abstract val isNullable: Boolean

    /**
     * Resolved from update.userOrNull
     */
    data class User(
        override val typeName: TypeName,
        override val isNullable: Boolean,
    ) : ParameterResolutionStrategy()

    /**
     * Resolved from update.chatOrNull
     */
    data class Chat(
        override val typeName: TypeName,
        override val isNullable: Boolean,
    ) : ParameterResolutionStrategy()

    /**
     * Resolved from context.bot
     */
    data class Bot(
        override val typeName: TypeName,
    ) : ParameterResolutionStrategy() {
        override val isNullable: Boolean = false
    }

    /**
     * Resolved from processing context
     */
    data class ProcessingContext(
        override val typeName: TypeName,
    ) : ParameterResolutionStrategy() {
        override val isNullable: Boolean = false
    }

    /**
     * Resolved from parameters map by parameter name
     */
    data class StringParameter(
        override val typeName: TypeName,
        override val isNullable: Boolean,
        val parameterName: String,
    ) : ParameterResolutionStrategy()

    /**
     * Resolved from parameters map and converted to primitive
     */
    sealed class PrimitiveParameter(
        override val typeName: TypeName,
        override val isNullable: Boolean,
        val parameterName: String,
        val conversionMethod: String,
    ) : ParameterResolutionStrategy() {
        class Int(typeName: TypeName, isNullable: Boolean, parameterName: String) :
            PrimitiveParameter(typeName, isNullable, parameterName, "toIntOrNull()")

        class Long(typeName: TypeName, isNullable: Boolean, parameterName: String) :
            PrimitiveParameter(typeName, isNullable, parameterName, "toLongOrNull()")

        class Short(typeName: TypeName, isNullable: Boolean, parameterName: String) :
            PrimitiveParameter(typeName, isNullable, parameterName, "toShortOrNull()")

        class Float(typeName: TypeName, isNullable: Boolean, parameterName: String) :
            PrimitiveParameter(typeName, isNullable, parameterName, "toFloatOrNull()")

        class Double(typeName: TypeName, isNullable: Boolean, parameterName: String) :
            PrimitiveParameter(typeName, isNullable, parameterName, "toDoubleOrNull()")
    }

    /**
     * Base ProcessedUpdate
     */
    data class Update(
        override val typeName: TypeName,
    ) : ParameterResolutionStrategy() {
        override val isNullable: Boolean = false
    }

    /**
     * Specific update type (MessageUpdate, CallbackQueryUpdate, etc.)
     * Resolved by casting update
     */
    data class TypedUpdate(
        override val typeName: TypeName,
        override val isNullable: Boolean,
        val updateTypeSimpleName: String,
    ) : ParameterResolutionStrategy()

    /**
     * Injectable type resolved via classManager.getInstance
     */
    data class Injectable(
        override val typeName: TypeName,
        val injectableClassName: ClassName,
    ) : ParameterResolutionStrategy() {
        override val isNullable: Boolean = false
    }

    /**
     * Unsupported type - will generate error code
     */
    data class Unsupported(
        override val typeName: TypeName,
    ) : ParameterResolutionStrategy() {
        override val isNullable: Boolean = false
    }
}
