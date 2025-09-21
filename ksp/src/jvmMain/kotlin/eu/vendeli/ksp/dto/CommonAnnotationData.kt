package eu.vendeli.ksp.dto

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import eu.vendeli.ksp.utils.messageList
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.fqName

sealed class CommonAnnotationValue {
    abstract val value: Any

    class String(
        override val value: kotlin.String,
    ) : CommonAnnotationValue()
    class Regex(
        override val value: kotlin.text.Regex,
    ) : CommonAnnotationValue()

    internal fun toCommonMatcher(filter: kotlin.String, scope: List<UpdateType>): kotlin.String {
        val parametersString = buildString {
            if (filter != DefaultFilter::class.fqName) append(", filter = $filter::class")
            if (scope != messageList) append(", scope = setOf(${scope.joinToString()})")
        }
        return when (this) {
            is String -> "CommonMatcher.String(value = \"$value\"$parametersString)"
            is Regex ->
                "CommonMatcher.Regex(value = Regex(\"$value\"${
                    value.options.takeIf { it.isNotEmpty() }?.joinToString(prefix = " , setOf(", postfix = ")") { "RegexOption.$it" } ?: ""
                })$parametersString)"
        }
    }
}

data class CommonAnnotationData(
    val funQualifier: String,
    val funSimpleName: String,
    val value: CommonAnnotationValue,
    val filter: String,
    val argParser: String,
    val priority: Int,
    val rateLimits: RateLimits,
    val scope: List<UpdateType>,
    val funDeclaration: KSFunctionDeclaration,
)
