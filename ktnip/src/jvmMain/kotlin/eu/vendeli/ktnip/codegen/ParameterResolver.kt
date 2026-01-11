package eu.vendeli.ktnip.codegen

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.ktnip.dto.ParameterResolutionStrategy
import eu.vendeli.ktnip.utils.TypeConstants
import eu.vendeli.ktnip.utils.findAnnotationRecursively
import eu.vendeli.tgbot.annotations.ParamMapping

/**
 * Resolves function parameters to their appropriate resolution strategies.
 * Determines how each parameter should be obtained during Activity invocation.
 */
class ParameterResolver(
    private val injectableTypes: Map<TypeName, ClassName>,
) {
    /**
     * Resolves a parameter to its resolution strategy.
     *
     * @param parameter The KSP parameter to resolve
     * @return The resolution strategy for this parameter
     */
    fun resolve(parameter: KSValueParameter): ParameterResolutionStrategy {
        val parameterTypeName = parameter.type.toTypeName()
        val typeName = parameterTypeName.copy(nullable = false)
        val isNullable = parameterTypeName.isNullable

        // Get parameter name from @ParamMapping or use actual name
        val parameterName = parameter.annotations
            .findAnnotationRecursively(ParamMapping::class)
            ?.arguments
            ?.firstOrNull { it.name?.asString() == ParamMapping::name.name }
            ?.value as? String
            ?: parameter.name?.getShortName()
            ?: return ParameterResolutionStrategy.Unsupported(typeName)

        return when (typeName) {
            // Special types resolved from context
            TypeConstants.userClass -> ParameterResolutionStrategy.User(typeName, isNullable)
            TypeConstants.chatClass -> ParameterResolutionStrategy.Chat(typeName, isNullable)
            TypeConstants.botClass -> ParameterResolutionStrategy.Bot(typeName)
            TypeConstants.processingCtx -> ParameterResolutionStrategy.AdditionalContext(typeName)
            TypeConstants.updateClass -> ParameterResolutionStrategy.Update(typeName)

            // String and primitives from parameters map
            STRING -> ParameterResolutionStrategy.StringParameter(typeName, isNullable, parameterName)
            INT, TypeConstants.intPrimitiveType -> ParameterResolutionStrategy.PrimitiveParameter.Int(
                typeName,
                isNullable,
                parameterName,
            )
            LONG, TypeConstants.longPrimitiveType -> ParameterResolutionStrategy.PrimitiveParameter.Long(
                typeName,
                isNullable,
                parameterName,
            )
            SHORT, TypeConstants.shortPrimitiveType -> ParameterResolutionStrategy.PrimitiveParameter.Short(
                typeName,
                isNullable,
                parameterName,
            )
            FLOAT, TypeConstants.floatPrimitiveType -> ParameterResolutionStrategy.PrimitiveParameter.Float(
                typeName,
                isNullable,
                parameterName,
            )
            DOUBLE, TypeConstants.doublePrimitiveType -> ParameterResolutionStrategy.PrimitiveParameter.Double(
                typeName,
                isNullable,
                parameterName,
            )

            // Typed updates
            in TypeMapper.getAllTypedUpdateTypes() -> {
                val simpleName = TypeMapper.getUpdateTypeSimpleName(typeName)
                    ?: return ParameterResolutionStrategy.Unsupported(typeName)
                ParameterResolutionStrategy.TypedUpdate(typeName, isNullable, simpleName)
            }

            // Injectable types
            in injectableTypes.keys -> {
                val injectableClass = injectableTypes[typeName]
                    ?: return ParameterResolutionStrategy.Unsupported(typeName)
                ParameterResolutionStrategy.Injectable(typeName, injectableClass)
            }

            // Unsupported type
            else -> ParameterResolutionStrategy.Unsupported(typeName)
        }
    }

    /**
     * Resolves all parameters of a function.
     *
     * @param parameters The list of parameters to resolve
     * @return Map of parameter index to resolution strategy
     */
    fun resolveAll(parameters: List<KSValueParameter>): Map<Int, ParameterResolutionStrategy> =
        parameters.mapIndexedNotNull { index, param ->
            if (param.name == null) null
            else index to resolve(param)
        }.toMap()
}
