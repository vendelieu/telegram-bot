package eu.vendeli.ksp.utils

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import eu.vendeli.ksp.dto.AnnotationData
import eu.vendeli.ksp.dto.CommonAnnotationData
import eu.vendeli.ksp.dto.CommonAnnotationValue
import eu.vendeli.tgbot.annotations.ArgParser
import eu.vendeli.tgbot.annotations.Guard
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits

internal fun List<KSValueArgument>.parseAsCommandHandler(isCallbackQ: Boolean) = AnnotationData(
    value = parseValueList(),
    rateLimits = parseRateLimits(),
    scope = parseScopes() ?: if (isCallbackQ) callbackQueryList else messageList,
    guardClass = parseGuard(),
    argParserClass = parseArgParser(),
    isAutoAnswer = firstOrNull { it.name?.asString() == "autoAnswer" }?.value?.safeCast<Boolean>() == true,
)

internal fun List<KSValueArgument>.parseAsInputHandler() = Triple(
    parseValueList(),
    parseRateLimits(),
    parseGuard(),
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
        val argParser = arguments.parseArgParser()

        if (value is List<*>) {
            value.forEach {
                commonAnnotations.add(
                    CommonAnnotationData(
                        funQualifier = qualifier,
                        funSimpleName = simpleName,
                        value = CommonAnnotationValue.String(it.cast()),
                        filter = filter,
                        argParser = argParser,
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
                    argParser = argParser,
                    priority = priority,
                    rateLimits = RateLimits(rateLimits.first, rateLimits.second),
                    scope = scope,
                    funDeclaration = function,
                ),
            )
        }
    }

    fun collect(): List<CommonAnnotationData> = commonAnnotations.sortedBy { it.priority }.also {
        commonAnnotations = mutableListOf()
    }
}

internal fun KSFunctionDeclaration.parseAnnotatedGuard(): String? = annotations
    .firstOrNull {
        it.shortName.asString() == Guard::class.simpleName!!
    }?.arguments
    ?.parseGuard()
    ?: closestClassDeclaration()
        ?.annotations
        ?.firstOrNull {
            it.shortName.asString() == Guard::class.simpleName!!
        }?.arguments
        ?.parseGuard()

internal fun KSFunctionDeclaration.parseAnnotatedRateLimits(): RateLimits? = annotations
    .firstOrNull {
        it.shortName.asString() == eu.vendeli.tgbot.annotations.RateLimits::class.simpleName!!
    }?.arguments
    ?.parseRateLimitsAnnotation()
    ?: closestClassDeclaration()
        ?.annotations
        ?.firstOrNull {
            it.shortName.asString() == eu.vendeli.tgbot.annotations.RateLimits::class.simpleName!!
        }?.arguments
        ?.parseRateLimitsAnnotation()

internal fun KSFunctionDeclaration.parseAnnotatedArgParser(): String? = annotations
    .firstOrNull {
        it.shortName.asString() == ArgParser::class.simpleName!!
    }?.arguments
    ?.parseArgParser()
    ?: closestClassDeclaration()
        ?.annotations
        ?.firstOrNull {
            it.shortName.asString() == ArgParser::class.simpleName!!
        }?.arguments
        ?.parseArgParser()

internal fun KSFunctionDeclaration.isThereAnnotation(vararg annotation: String): Boolean = annotations.any {
    it.shortName.asString() in annotation
}

/*
 argument parsers:
*/

internal fun List<KSValueArgument>.parseScopes(): List<UpdateType>? = firstOrNull {
    it.name?.asString() == "scope"
}?.value?.safeCast<List<*>>()?.map {
    val value = when (it) {
        is KSType -> it.declaration.toString()
        is KSClassDeclaration -> it.simpleName.getShortName()
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

internal fun List<KSValueArgument>.parseArgParser(): String =
    firstOrNull { it.name?.asString() == "argParser" }?.value?.safeCast<KSType>()?.let {
        it.declaration.qualifiedName!!.asString()
    } ?: DefaultArgParser::class.qualifiedName!!

internal fun List<KSValueArgument>.parseAsUpdateHandler() = first().value.cast<List<*>>().map { i ->
    when (i) {
        is KSType -> i.declaration.toString()
        is KSClassDeclaration -> i.simpleName.getShortName()
        else -> throw IllegalStateException("Unknown type $i")
    }.let { UpdateType.valueOf(it) }
}

internal fun List<KSValueArgument>.parseRateLimitsAnnotation(): RateLimits =
    ((firstOrNull()?.value?.safeCast<Long>() ?: 0) to (lastOrNull()?.value?.safeCast<Long>() ?: 0)).toRateLimits()

internal fun List<KSValueArgument>.parseRateLimits(): Pair<Long, Long> = firstOrNull {
    it.name?.asString() == "rateLimits"
}?.value?.safeCast<KSAnnotation>()?.arguments?.let {
    (it.firstOrNull()?.value?.safeCast<Long>() ?: 0) to (it.lastOrNull()?.value?.safeCast() ?: 0)
} ?: notLimitedRateLimits
