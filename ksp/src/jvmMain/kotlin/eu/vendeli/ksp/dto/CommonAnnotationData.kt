package eu.vendeli.ksp.dto

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits

sealed class CommonAnnotationValue {
    abstract val value: Any

    class String(
        override val value: kotlin.String,
    ) : CommonAnnotationValue()
    class Regex(
        override val value: kotlin.text.Regex,
    ) : CommonAnnotationValue()

    internal fun toCommonMatcher(filter: kotlin.String, scope: List<UpdateType>) = when (this) {
        is String -> "CommonMatcher.String(value = \"$value\", filter = $filter::class, setOf(${scope.joinToString()}))"
        is Regex ->
            "CommonMatcher.Regex(value = Regex(\"$value\"${
                value.options.takeIf { it.isNotEmpty() }?.joinToString(prefix = " ,") { "RegexOption.$it" } ?: ""
            }), filter = $filter::class, setOf(${scope.joinToString()}))"
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
