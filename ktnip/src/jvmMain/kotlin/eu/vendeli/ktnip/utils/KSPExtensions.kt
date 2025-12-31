package eu.vendeli.ktnip.utils

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlin.reflect.KClass

/**
 * KSP-specific extension functions.
 */

/**
 * Converts KSType to KSP ClassName or TypeName based on type arguments.
 */
fun KSType.toKSPClassName(): TypeName = starProjection().let {
    if (arguments.isEmpty()) it.toClassName() else it.toTypeName()
}

/**
 * Checks if this sequence of annotations contains [target],
 * either directly or via meta-annotations (recursively).
 */
fun Sequence<KSAnnotation>.hasAnnotationRecursively(
    target: KClass<out Annotation>,
): Boolean {
    val targetFqName = target.qualifiedName ?: return false

    fun KSAnnotation.matchesOrMetaVisited(visited: MutableSet<String>): Boolean {
        val fqName = annotationType.resolve().declaration.qualifiedName?.asString() ?: return false
        if (fqName == targetFqName) return true
        if (!visited.add(fqName)) return false // already checked -> avoid cycles

        val annoDecl = annotationType.resolve().declaration as? KSClassDeclaration
            ?: return false

        // recurse into the annotations on this annotation class
        return annoDecl.annotations.any { it.matchesOrMetaVisited(visited) }
    }

    val visited = mutableSetOf<String>()
    return any { it.matchesOrMetaVisited(visited) }
}

/**
 * Finds the first annotation matching [target], directly or via meta-annotations (recursively).
 *
 * @return the matching KSAnnotation, or null if not found.
 */
fun Sequence<KSAnnotation>.findAnnotationRecursively(
    target: KClass<out Annotation>,
): KSAnnotation? {
    val targetFqName = target.qualifiedName ?: return null

    fun KSAnnotation.findRecursive(visited: MutableSet<String>): KSAnnotation? {
        val fqName = annotationType.resolve().declaration.qualifiedName?.asString() ?: return null
        if (fqName == targetFqName) return this
        if (!visited.add(fqName)) return null // already visited → avoid cycles

        val annoDecl = annotationType.resolve().declaration as? KSClassDeclaration
            ?: return null

        // Recurse into the annotations present on this annotation class
        for (nested in annoDecl.annotations) {
            val found = nested.findRecursive(visited)
            if (found != null) return found
        }
        return null
    }

    val visited = mutableSetOf<String>()
    for (anno in this) {
        val found = anno.findRecursive(visited)
        if (found != null) return found
    }
    return null
}

/**
 * Expands this annotation recursively into all base annotations that match [targets].
 */
fun KSAnnotation.expandToBaseAnnotations(
    targets: Set<String>,
    visited: MutableSet<String> = mutableSetOf(),
): Sequence<KSAnnotation> {
    val fqName = annotationType.resolve().declaration.qualifiedName?.asString() ?: return emptySequence()
    if (!visited.add(fqName)) return emptySequence()

    // If this annotation is itself one of the targets → yield it
    if (fqName in targets) return sequenceOf(this)

    // Otherwise recurse into annotations on its annotation class
    val annoDecl = annotationType.resolve().declaration as? KSClassDeclaration ?: return emptySequence()
    return annoDecl.annotations.flatMap { it.expandToBaseAnnotations(targets, visited) }
}

/**
 * Type-safe cast helper.
 */
@Suppress("UNCHECKED_CAST")
inline fun <R> Any?.cast(): R = this as R

/**
 * Type-safe safe cast helper.
 */
@Suppress("UNCHECKED_CAST")
inline fun <R> Any?.safeCast(): R? = this as? R
