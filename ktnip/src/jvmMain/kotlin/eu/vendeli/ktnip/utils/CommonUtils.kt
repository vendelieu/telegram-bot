package eu.vendeli.ktnip.utils

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import eu.vendeli.tgbot.types.configuration.RateLimits

/**
 * Common utility functions.
 */

typealias FileBuilder = com.squareup.kotlinpoet.FileSpec.Builder

const val DEFAULT_CODEGEN_PACKAGE = "eu.vendeli.tgbot.generated"

/**
 * Converts a pair of Longs to RateLimits configuration.
 */
fun Pair<Long, Long>.toRateLimits(): RateLimits = RateLimits(first, second)

/**
 * Checks for inapplicable annotations on a function and warns if found.
 *
 * @param targetAnnotationName The name of the target annotation
 * @param logger KSP logger for warnings
 * @param annotation Names of annotations to check for
 */
fun KSFunctionDeclaration.checkForInapplicableAnnotations(
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
