package eu.vendeli.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.utils.DefaultFilter

internal fun List<KSValueArgument>.parseAsCommandHandler(isCallbackQ: Boolean) = AnnotationData(
    value = parseValueList(),
    rateLimits = parseRateLimits(),
    scope = firstOrNull { it.name?.asString() == "scope" }?.value?.safeCast<List<KSType>>()
        ?.map { UpdateType.valueOf(it.declaration.toString()) } ?: if (isCallbackQ) callbackQueryList else messageList,
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
    it.first().value.cast<Long>() to it.last().value.cast()
} ?: notLimitedRateLimits
