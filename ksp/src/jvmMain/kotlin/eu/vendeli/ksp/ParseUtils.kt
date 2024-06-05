package eu.vendeli.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.utils.DefaultFilter

internal fun List<KSValueArgument>.parseAsCommandHandler(isCallbackQ: Boolean) = AnnotationData(
    value = parseValueList(),
    rateLimits = parseRateLimits(),
    scope = firstOrNull { it.name?.asString() == "scope" }?.value?.safeCast<List<*>>()?.map {
        val value = when {
            it is KSType -> it.declaration.toString()
            it is KSClassDeclaration -> it.simpleName.getShortName()
            else -> throw IllegalStateException("Unknown type $it")
        }
        UpdateType.valueOf(value)
    } ?: if (isCallbackQ) callbackQueryList else messageList,
    guardClass = parseGuard(),
)

internal fun List<KSValueArgument>.parseAsInputHandler() = Triple(
    parseValueList(),
    parseRateLimits(),
    parseGuard(),
)

internal fun List<KSValueArgument>.parseAsRegexHandler() = Triple(
    first { it.name?.asString() == "value" }.value.cast<String>(),
    parseRateLimits(),
    firstOrNull { it.name?.asString() == "options" }?.value?.safeCast<List<RegexOption>>() ?: emptyList(),
)

/*
 argument parsers:
*/

internal fun List<KSValueArgument>.parseValueList(): List<String> = first {
    it.name?.asString() == "value"
}.value.cast()

internal fun List<KSValueArgument>.parseGuard(): String =
    firstOrNull { it.name?.asString() == "guard" }?.value?.safeCast<KSType>()?.let {
        it.declaration.qualifiedName!!.asString()
    } ?: DefaultFilter::class.qualifiedName!!

internal fun List<KSValueArgument>.parseAsUpdateHandler() = first().value.cast<List<KSType>>().map {
    UpdateType.valueOf(it.declaration.toString())
}

internal fun List<KSValueArgument>.parseRateLimits(): Pair<Long, Long> = firstOrNull {
    it.name?.asString() == "rateLimits"
}?.value?.safeCast<KSAnnotation>()?.arguments?.let {
    (it.firstOrNull()?.value?.safeCast<Long>() ?: 0) to (it.lastOrNull()?.value?.safeCast() ?: 0)
} ?: notLimitedRateLimits
