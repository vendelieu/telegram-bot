package eu.vendeli.ksp.utils

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.isDefault
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import eu.vendeli.ksp.dto.AnnotationData
import eu.vendeli.ksp.dto.CommonAnnotationData
import eu.vendeli.ksp.dto.CommonAnnotationValue
import eu.vendeli.tgbot.annotations.ArgParser
import eu.vendeli.tgbot.annotations.CommonHandler
import eu.vendeli.tgbot.annotations.Guard
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.configuration.RateLimits

internal fun List<KSValueArgument>.parseAsCommandHandler(isCallbackQ: Boolean) = AnnotationData(
    value = parseValueList(),
    scope = parseScopes() ?: if (isCallbackQ) callbackQueryList else messageList,
    isAutoAnswer = firstOrNull {
        it.name?.asString() == "autoAnswer"
    }?.takeIf { !it.isDefault() }?.value?.safeCast<Boolean>(),
)

internal fun List<KSValueArgument>.parseAsInputHandler() = parseValueList()

/**
 * Expands this annotation recursively into all base annotations that match [targets].
 */
internal fun KSAnnotation.expandToBaseAnnotations(
    targets: Set<String>,
    visited: MutableSet<String> = mutableSetOf()
): Sequence<KSAnnotation> {
    val fqName = annotationType.resolve().declaration.qualifiedName?.asString() ?: return emptySequence()
    if (!visited.add(fqName)) return emptySequence()

    // If this annotation is itself one of the targets → yield it
    if (fqName in targets) return sequenceOf(this)

    // Otherwise recurse into annotations on its annotation class
    val annoDecl = annotationType.resolve().declaration as? KSClassDeclaration ?: return emptySequence()
    return annoDecl.annotations.flatMap { it.expandToBaseAnnotations(targets, visited) }
}


object CommonAnnotationHandler {
    private var commonAnnotations = mutableListOf<CommonAnnotationData>()

    fun parse(function: KSFunctionDeclaration, arguments: List<KSValueArgument>) {
        val qualifier = function.qualifiedName!!.getQualifier()
        val simpleName = function.simpleName.asString()

        val value = arguments.first { it.name?.asString() == CommonHandler.Text::value.name }.value
        val filter = arguments.firstOrNull {
            it.name?.asString() == CommonHandler.Text::filter.name
        }?.value?.safeCast<KSType>()?.let {
            it.declaration.qualifiedName!!.asString()
        } ?: DefaultFilter::class.qualifiedName!!
        val priority = arguments.firstOrNull {
            it.name?.asString() == CommonHandler.Text::priority.name
        }?.value?.safeCast<Int>() ?: 0

        val rateLimits = function.parseAnnotatedRateLimits()
        val scope = arguments.parseScopes() ?: messageList
        val argParser = function.parseAnnotatedArgParser()

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
                        rateLimits = rateLimits,
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
                    rateLimits = rateLimits,
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
    .findAnnotationRecursively(Guard::class)
    ?.arguments
    ?.parseGuard()
    ?: closestClassDeclaration()
        ?.annotations
        ?.findAnnotationRecursively(Guard::class)
        ?.arguments
        ?.parseGuard()

internal fun KSFunctionDeclaration.parseAnnotatedRateLimits(): RateLimits = annotations
    .findAnnotationRecursively(eu.vendeli.tgbot.annotations.RateLimits::class)
    ?.arguments
    ?.parseRateLimitsAnnotation()
    ?: closestClassDeclaration()
        ?.annotations
        ?.findAnnotationRecursively(eu.vendeli.tgbot.annotations.RateLimits::class)
        ?.arguments
        ?.parseRateLimitsAnnotation() ?: RateLimits(0, 0)

internal fun KSFunctionDeclaration.parseAnnotatedArgParser(): String = annotations
    .findAnnotationRecursively(ArgParser::class)
    ?.arguments
    ?.parseArgParser()
    ?: closestClassDeclaration()
        ?.annotations
        ?.findAnnotationRecursively(ArgParser::class)
        ?.arguments
        ?.parseArgParser() ?: DefaultArgParser::class.qualifiedName!!

internal fun KSFunctionDeclaration.checkForInapplicableAnnotations(
    targetAnnotationName: String,
    logger: KSPLogger,
    vararg annotation: String,
) {
    val warningMessage = StringBuilder()
    annotations
        .filter {
            it.shortName.asString() in annotation
        }.forEach {
            warningMessage.appendLine(
                "Be aware that @${it.shortName.asString()} is not supported for $targetAnnotationName",
            )
        }
    if (warningMessage.isNotEmpty()) logger.warn(warningMessage.toString())
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
    firstOrNull { it.name?.asString() == CommonHandler.Regex::options.name }?.value?.safeCast<List<*>>()?.map { i ->
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
    firstOrNull { it.name?.asString() == Guard::guard.name }?.value?.safeCast<KSType>()?.let {
        it.declaration.qualifiedName!!.asString()
    } ?: DefaultGuard::class.qualifiedName!!

internal fun List<KSValueArgument>.parseArgParser(): String =
    firstOrNull { it.name?.asString() == ArgParser::argParser.name }?.value?.safeCast<KSType>()?.let {
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
