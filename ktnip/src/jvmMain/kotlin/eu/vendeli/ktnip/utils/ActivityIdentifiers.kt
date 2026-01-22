package eu.vendeli.ktnip.utils

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

private const val STRING_RADIX = 16

/**
 * Functions for generating activity IDs and object names.
 */

/**
 * Gets the signature of a function for ID generation.
 * Format: qualifier::functionName(param1Type, param2Type, ...)
 */
fun KSFunctionDeclaration.getSignature(): String {
    val params = parameters.joinToString(prefix = "(", postfix = ")") {
        it.type.resolve().declaration.qualifiedName?.asString() ?: ""
    }
    val qualifiedName = qualifiedName?.asString() ?: ""
    val lastDot = qualifiedName.lastIndexOf('.')
    val nameWithDoubleColon = if (lastDot != -1) {
        qualifiedName.substring(0, lastDot) + "::" + qualifiedName.substring(lastDot + 1)
    } else {
        "::$qualifiedName"
    }
    return "$nameWithDoubleColon$params"
}

/**
 * Gets the activity ID (hash of signature).
 */
fun KSFunctionDeclaration.getActivityId(): Int = getSignature().hashCode()

/**
 * Gets the generated object name for this activity.
 * Format: qualifier_functionName_idHex
 */
fun KSFunctionDeclaration.getActivityObjectName(): String {
    val funQualifier = qualifiedName!!.getQualifier()
    val funShortName = simpleName.getShortName()
    val id = getActivityId()
    val prefix = funQualifier.replace(".", "_")
    return "${prefix}_${funShortName}_${id.toUInt().toString(STRING_RADIX)}"
}

/**
 * Gets the activity ID for a class (used in input chains).
 */
fun KSClassDeclaration.getActivityId(): Int = qualifiedName!!.asString().hashCode()

/**
 * Gets the generated object name for a class activity.
 */
fun KSClassDeclaration.getActivityObjectName(): String {
    val qualifier = qualifiedName!!.getQualifier()
    val name = simpleName.asString()
    val id = getActivityId()
    val prefix = qualifier.replace(".", "_")
    return "${prefix}_${name}_${id.toUInt().toString(STRING_RADIX)}"
}
