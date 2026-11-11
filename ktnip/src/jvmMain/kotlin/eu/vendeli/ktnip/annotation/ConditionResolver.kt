package eu.vendeli.ktnip.annotation

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.isDefault
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock
import eu.vendeli.tgbot.annotations.ConditionOf
import eu.vendeli.tgbot.interfaces.helper.UpdateMatcher

/**
 * Resolves user-defined condition annotations (those meta-annotated with [ConditionOf]) found on a function,
 * directly or through composed annotations, into Kotlin expressions that evaluate to `UpdateCondition`.
 */
internal object ConditionResolver {
    private val conditionOfFqName = ConditionOf::class.qualifiedName!!
    private val matcherFqName = UpdateMatcher::class.qualifiedName!!

    private val primitiveArrays = mapOf(
        "kotlin.IntArray" to "intArrayOf",
        "kotlin.LongArray" to "longArrayOf",
        "kotlin.ShortArray" to "shortArrayOf",
        "kotlin.ByteArray" to "byteArrayOf",
        "kotlin.BooleanArray" to "booleanArrayOf",
        "kotlin.CharArray" to "charArrayOf",
        "kotlin.FloatArray" to "floatArrayOf",
        "kotlin.DoubleArray" to "doubleArrayOf",
    )

    /**
     * @return one expression per condition annotation, in declaration order. Reports an error through
     * [logger] (failing the build) for malformed conditions and omits them from the result.
     */
    fun resolve(function: KSFunctionDeclaration, logger: KSPLogger): List<String> {
        val result = mutableListOf<String>()

        fun walk(annotation: KSAnnotation, path: Set<String>) {
            val declaration = annotation.annotationType.resolve().declaration as? KSClassDeclaration ?: return
            val fqName = declaration.qualifiedName?.asString() ?: return
            if (fqName in path) return

            declaration.annotations
                .firstOrNull { it.fqName() == conditionOfFqName }
                ?.let { conditionOf -> buildCondition(annotation, declaration, conditionOf, logger)?.let(result::add) }

            declaration.annotations.forEach { walk(it, path + fqName) }
        }

        function.annotations.forEach { walk(it, emptySet()) }
        return result
    }

    private fun buildCondition(
        usage: KSAnnotation,
        annotationDecl: KSClassDeclaration,
        conditionOf: KSAnnotation,
        logger: KSPLogger,
    ): String? {
        val annotationFq = annotationDecl.qualifiedName!!.asString()
        val matcherDecl = (conditionOf.arguments.firstOrNull()?.value as? KSType)?.declaration as? KSClassDeclaration
        if (matcherDecl == null) {
            logger.error("@ConditionOf on $annotationFq must reference a matcher class.", annotationDecl)
            return null
        }
        val matcherFq = matcherDecl.qualifiedName!!.asString()

        val matcherArg = matcherDecl.getAllSuperTypes()
            .firstOrNull { it.declaration.qualifiedName?.asString() == matcherFqName }
            ?.arguments?.firstOrNull()?.type?.resolve()?.declaration?.qualifiedName?.asString()
        if (matcherArg != annotationFq) {
            logger.error(
                "$matcherFq must implement UpdateMatcher<$annotationFq> to be used in @ConditionOf on $annotationFq" +
                    (matcherArg?.let { " (found UpdateMatcher<$it>)" } ?: ""),
                matcherDecl,
            )
            return null
        }

        val matcherExpr = when {
            matcherDecl.classKind == ClassKind.OBJECT -> matcherFq
            matcherDecl.primaryConstructor?.parameters?.all { it.hasDefault } ?: true -> "$matcherFq()"
            else -> {
                logger.error("Matcher $matcherFq must be an object or have a no-arg constructor.", matcherDecl)
                return null
            }
        }

        val annotationExpr = renderAnnotation(usage, annotationDecl, annotationFq, logger) ?: return null
        return "$matcherExpr.bind($annotationExpr)"
    }

    private fun renderAnnotation(
        usage: KSAnnotation,
        declaration: KSClassDeclaration,
        fqName: String,
        logger: KSPLogger,
    ): String? {
        val params = declaration.primaryConstructor?.parameters.orEmpty().associateBy { it.name?.asString() }
        val args = usage.arguments.filterNot { it.isDefault() }.map { arg ->
            val name = arg.name?.asString()
            val arrayType = params[name]?.type?.resolve()?.declaration?.qualifiedName?.asString()
            val rendered = renderValue(arg.value, arrayType, declaration, logger) ?: return null
            "$name = $rendered"
        }
        return "$fqName(${args.joinToString(", ")})"
    }

    private fun renderValue(
        value: Any?,
        paramTypeFq: String?,
        owner: KSClassDeclaration,
        logger: KSPLogger,
    ): String? = when (value) {
        is String -> CodeBlock.of("%S", value).toString()
        is Boolean, is Int, is Double -> value.toString()
        is Long -> "${value}L"
        is Float -> "${value}f"
        is Short -> "$value.toShort()"
        is Byte -> "$value.toByte()"
        is Char -> CodeBlock.of("%L", "'${value.toString().replace("\\", "\\\\").replace("'", "\\'")}'").toString()
        is KSType -> renderType(value)
        is KSClassDeclaration -> value.qualifiedName?.asString()
        is List<*> -> renderArray(value, paramTypeFq, owner, logger)
        else -> {
            logger.error(
                "Unsupported argument value of type ${value?.let { it::class.simpleName }} " +
                    "in condition annotation ${owner.qualifiedName?.asString()}.",
                owner,
            )
            null
        }
    }

    private fun renderType(type: KSType): String? {
        val declaration = type.declaration
        val fq = declaration.qualifiedName?.asString() ?: return null
        val isEnumEntry = (declaration as? KSClassDeclaration)?.classKind == ClassKind.ENUM_ENTRY
        return if (isEnumEntry) fq else "$fq::class"
    }

    private fun renderArray(
        values: List<*>,
        paramTypeFq: String?,
        owner: KSClassDeclaration,
        logger: KSPLogger,
    ): String? {
        val elements = values.map { renderValue(it, null, owner, logger) ?: return null }
        val factory = primitiveArrays[paramTypeFq] ?: "arrayOf"
        return "$factory(${elements.joinToString(", ")})"
    }

    private fun KSAnnotation.fqName(): String? =
        annotationType.resolve().declaration.qualifiedName?.asString()
}
