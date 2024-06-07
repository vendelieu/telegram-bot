package eu.vendeli.ksp.utils

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import eu.vendeli.ksp.dto.AnnotationData
import eu.vendeli.ksp.dto.CommonAnnotationData
import eu.vendeli.ksp.dto.CommonAnnotationValue
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.DefaultFilter
import eu.vendeli.tgbot.utils.DefaultGuard

internal fun List<KSValueArgument>.parseAsCommandHandler(isCallbackQ: Boolean) = AnnotationData(
    value = parseValueList(),
    rateLimits = parseRateLimits(),
    scope = parseScopes() ?: if (isCallbackQ) callbackQueryList else messageList,
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
    parseRegexOptions(),
)

object CommonAnnotationHandler {
    private var commonAnnotations = mutableListOf<CommonAnnotationData>()

    fun parse(function: KSFunctionDeclaration, arguments: List<KSValueArgument>) {
        val qualifier = function.qualifiedName!!.getQualifier()
        val simpleName = function.simpleName.asString()

        val value = arguments.first { it.name?.asString() == "value" }.value
        val filter = arguments.firstOrNull { it.name?.asString() == "filter" }?.value?.safeCast<KSType>()?.let {
            it.declaration.qualifiedName!!.asString()
        } ?: DefaultFilter::class.qualifiedName!!
        val priority = arguments.firstOrNull { it.name?.asString() == "priority" }?.value?.safeCast<Int>() ?: 0
        val rateLimits = arguments.parseRateLimits()
        val scope = arguments.parseScopes() ?: messageList

        if (value is List<*>) {
            value.forEach {
                commonAnnotations.add(
                    CommonAnnotationData(
                        funQualifier = qualifier,
                        funSimpleName = simpleName,
                        value = CommonAnnotationValue.String(it.cast()),
                        filter = filter,
                        priority = priority,
                        rateLimits = RateLimits(rateLimits.first, rateLimits.second),
                        scope = scope,
                        funDeclaration = function,
                    ),
                )
            }
        }

        if (value is String) {
            val regexOptions = arguments.parseRegexOptions()
            commonAnnotations.add(
                CommonAnnotationData(
                    funQualifier = qualifier,
                    funSimpleName = simpleName,
                    value = CommonAnnotationValue.Regex(value.toRegex(regexOptions.toSet())),
                    filter = filter,
                    priority = priority,
                    rateLimits = RateLimits(rateLimits.first, rateLimits.second),
                    scope = scope,
                    funDeclaration = function,
                ),
            )
        }
    }

    fun collect(): List<CommonAnnotationData> {
        return commonAnnotations.sortedBy { it.priority }.also {
            commonAnnotations = mutableListOf()
        }
    }
}

/*
 argument parsers:
*/

internal fun List<KSValueArgument>.parseScopes(): List<UpdateType>? = firstOrNull {
    it.name?.asString() == "scope"
}?.value?.safeCast<List<*>>()?.map {
    val value = when {
        it is KSType -> it.declaration.toString()
        it is KSClassDeclaration -> it.simpleName.getShortName()
        else -> throw IllegalStateException("Unknown type $it")
    }
    UpdateType.valueOf(value)
}

internal fun List<KSValueArgument>.parseRegexOptions(): List<RegexOption> =
    firstOrNull { it.name?.asString() == "options" }?.value?.safeCast<List<*>>()?.map { i ->
        when (i) {
            is KSType -> i.declaration.toString()
            is KSClassDeclaration -> i.simpleName.getShortName()
            else -> throw IllegalStateException("Unknown type $i")
        }.let { RegexOption.valueOf(it) }
    } ?: emptyList()

internal fun List<KSValueArgument>.parseValueList(): List<String> = first {
    it.name?.asString() == "value"
}.value.cast()

internal fun List<KSValueArgument>.parseGuard(): String =
    firstOrNull { it.name?.asString() == "guard" }?.value?.safeCast<KSType>()?.let {
        it.declaration.qualifiedName!!.asString()
    } ?: DefaultGuard::class.qualifiedName!!

internal fun List<KSValueArgument>.parseAsUpdateHandler() = first().value.cast<List<KSType>>().map {
    UpdateType.valueOf(it.declaration.toString())
}

internal fun List<KSValueArgument>.parseRateLimits(): Pair<Long, Long> = firstOrNull {
    it.name?.asString() == "rateLimits"
}?.value?.safeCast<KSAnnotation>()?.arguments?.let {
    (it.firstOrNull()?.value?.safeCast<Long>() ?: 0) to (it.lastOrNull()?.value?.safeCast() ?: 0)
} ?: notLimitedRateLimits
