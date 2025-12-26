package eu.vendeli.ksp.utils

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.tgbot.annotations.Injectable
import kotlin.reflect.KClass

/**
 * Extensions for KSP Resolver to find annotated symbols.
 */

/**
 * Gets injectable types annotated with @Injectable.
 * Returns a map of TypeName to ClassName for autowiring.
 */
fun Resolver.getInjectableTypes(pkg: String?): Map<TypeName, ClassName> =
    getAnnotatedClassSymbols(Injectable::class, pkg).associate { c ->
        c
            .getAllSuperTypes()
            .first {
                it.declaration.qualifiedName!!.asString() == TypeConstants.autowiringFQName
            }.arguments
            .first()
            .toTypeName() to
            c.toClassName()
    }

/**
 * Gets function symbols annotated with specified annotations,
 * including support for meta-annotations.
 *
 * @param targetPackage Optional package filters
 * @param kClasses Annotation classes to search for
 * @return Sequence of annotated functions
 */
fun <T : Annotation> Resolver.getAnnotatedFnSymbols(
    targetPackage: String? = null,
    vararg kClasses: KClass<out T>,
): Sequence<KSFunctionDeclaration> {
    val targetAnnoNames = kClasses.mapNotNull { it.qualifiedName }.toSet()

    // step 1: find meta-annotations (annotations that carry our target ones)
    val metaAnnotations = getAllFiles()
        .flatMap { it.declarations }
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.classKind == ClassKind.ANNOTATION_CLASS }
        .filter { annoDecl ->
            annoDecl.annotations.any { ann ->
                val fqName = ann.annotationType.resolve().declaration.qualifiedName?.asString()
                fqName in targetAnnoNames
            }
        }
        .mapNotNull { it.qualifiedName?.asString() }
        .toSet()

    val allTargetNames = targetAnnoNames + metaAnnotations

    // step 2: gather functions annotated with either direct or meta annotation
    val candidates = allTargetNames
        .asSequence()
        .flatMap { annoName -> getSymbolsWithAnnotation(annoName) }
        .filterIsInstance<KSFunctionDeclaration>()

    return if (targetPackage != null) {
        candidates.filter { f -> f.packageName.asString().startsWith(targetPackage) }
    } else {
        candidates
    }
}

/**
 * Gets class symbols annotated with specified annotation.
 *
 * @param clazz Annotation class to search for
 * @param targetPackage Optional package filters
 * @return Sequence of annotated classes
 */
fun <T : Annotation> Resolver.getAnnotatedClassSymbols(
    clazz: KClass<T>,
    targetPackage: String? = null,
): Sequence<KSClassDeclaration> =
    if (targetPackage == null) {
        getSymbolsWithAnnotation(clazz.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
    } else {
        getSymbolsWithAnnotation(clazz.qualifiedName!!)
            .filter {
                it is KSClassDeclaration && it.packageName.asString().startsWith(targetPackage)
            }.cast()
    }
