package eu.vendeli.ktnip.dto

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import eu.vendeli.ktnip.utils.TypeConstants.messageList
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.configuration.RateLimits

sealed class CommonAnnotationValue {
    abstract val value: Any

    class String(
        override val value: kotlin.String,
    ) : CommonAnnotationValue()
    class Regex(
        override val value: kotlin.text.Regex,
    ) : CommonAnnotationValue()

    internal fun toCommonMatcher(filters: List<kotlin.String>, scope: List<UpdateType>): kotlin.String {
        val parametersString = buildString {
            if (filters.isNotEmpty()) append(
                ", filters = setOf(${filters.joinToString { "$it::class" }})",
            )
            if (scope != messageList) append(", scope = setOf(${scope.joinToString()})")
        }
        return when (this) {
            is String -> "CommonMatcher.String(value = \"$value\"$parametersString)"
            is Regex ->
                "CommonMatcher.Regex(value = Regex(\"$value\"${
                    value.options.takeIf { it.isNotEmpty() }?.joinToString(
                        prefix = " , setOf(",
                        postfix = ")",
                    ) { "RegexOption.$it" } ?: ""
                })$parametersString)"
        }
    }

    override fun toString(): kotlin.String = toCommonMatcher(emptyList(), emptyList())
}

data class CommonAnnotationData(
    val funQualifier: String,
    val funSimpleName: String,
    val value: CommonAnnotationValue,
    val filters: List<String>,
    val argParser: String,
    val priority: Int,
    val rateLimits: RateLimits,
    val scope: List<UpdateType>,
    val funDeclaration: KSFunctionDeclaration,
)
