package eu.vendeli.ktnip.codegen

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.utils.common.fqName

/**
 * Generates GraalVM native-image metadata for the classes the generated code resolves at runtime:
 * - classes handed to `ClassManager` (created with kotlin-reflect by the default `ClassManagerImpl`):
 *   the primary constructor (or `INSTANCE` of an object), plus supertypes and constructor parameter
 *   types, which kotlin-reflect looks up while resolving the constructor
 * - the generated `ContextLoader` classes, which the library finds through `ServiceLoader`
 *
 * kotlin-reflect's own metadata ships with the telegram-bot jar.
 */
@OptIn(KspExperimental::class)
class NativeMetadataGenerator(
    private val resolver: Resolver,
    private val logger: KSPLogger,
) {
    private val reflection = sortedMapOf<String, Entry>()
    private val visitedTypes = mutableSetOf<String>()

    private class Entry {
        val fields = sortedSetOf<String>()
        val methods = sortedMapOf<String, List<String>>() // signature key -> parameter types
    }

    /** Registers a class that will be instantiated through `ClassManager`. */
    fun addInstantiated(qualifiedName: String) {
        if (qualifiedName in DEFAULTS) return
        val declaration = resolver.getClassDeclarationByName(qualifiedName)
        if (declaration == null) {
            logger.warn("Native metadata: class $qualifiedName not found, skipping it.")
            return
        }
        val entry = entry(declaration.binaryName())

        if (declaration.classKind == ClassKind.OBJECT) {
            entry.fields += "INSTANCE"
        } else {
            val constructors = declaration.primaryConstructor?.let(::listOf) ?: declaration.getConstructors().toList()
            constructors.forEach { constructor ->
                val parameterTypes = constructor.jvmParameterTypes() ?: return@forEach
                entry.methods[parameterTypes.joinToString(prefix = "<init>(", postfix = ")")] = parameterTypes
                constructor.parameters.forEach { addTypeTree(it.type.resolve()) }
            }
        }
        declaration.getAllSuperTypes().forEach { addTypeTree(it) }
        addEnclosingClasses(declaration)
    }

    /** kotlin-reflect resolves a nested class through its enclosing classes, so those must be registered too. */
    private fun addEnclosingClasses(declaration: KSClassDeclaration) {
        generateSequence(declaration.parentDeclaration as? KSClassDeclaration) {
            it.parentDeclaration as? KSClassDeclaration
        }.forEach { entry(it.binaryName()) }
    }

    /** Registers a generated `ContextLoader`, created through `ServiceLoader` with its no-arg constructor. */
    fun addServiceProvider(binaryName: String) {
        entry(binaryName).methods["<init>()"] = emptyList()
    }

    fun write(codeGenerator: CodeGenerator, dependencies: Dependencies) {
        val registrations = reflection.map { (type, entry) -> type to entry }
        write(codeGenerator, dependencies, "reachability-metadata", newFormat(registrations))
        write(codeGenerator, dependencies, "reflect-config", legacyReflection(registrations))
        write(codeGenerator, dependencies, "resource-config", legacyResources())
    }

    private fun write(codeGenerator: CodeGenerator, dependencies: Dependencies, name: String, content: Any) {
        codeGenerator
            .createNewFileByPath(dependencies, "$OUTPUT_DIR/$name", "json")
            .bufferedWriter()
            .use { it.write(MetadataJson.render(content) + "\n") }
    }

    private fun newFormat(registrations: List<Pair<String, Entry>>) = linkedMapOf(
        "reflection" to registrations.map { (type, entry) -> entry.toJson("type", type) },
        "resources" to listOf(mapOf("glob" to SERVICES_FILE)),
    )

    private fun legacyReflection(registrations: List<Pair<String, Entry>>) =
        registrations.map { (type, entry) -> entry.toJson("name", type) }

    private fun legacyResources() = mapOf(
        "resources" to mapOf("includes" to listOf(mapOf("pattern" to "\\Q$SERVICES_FILE\\E"))),
    )

    private fun Entry.toJson(typeKey: String, type: String): Map<String, Any> = linkedMapOf<String, Any>().apply {
        put(typeKey, type)
        if (fields.isNotEmpty()) put("fields", fields.map { mapOf("name" to it) })
        if (methods.isNotEmpty()) {
            put(
                "methods",
                methods.values.map { linkedMapOf("name" to "<init>", "parameterTypes" to it) },
            )
        }
    }

    private fun entry(binaryName: String) = reflection.getOrPut(binaryName, ::Entry)

    /** Registers [type] and, recursively, its type arguments as plain types (no members). */
    private fun addTypeTree(type: KSType) {
        val declaration = when (val decl = type.declaration) {
            is KSTypeAlias -> return addTypeTree(decl.type.resolve())
            is KSClassDeclaration -> decl
            else -> return // type parameters
        }
        val name = declaration.binaryName()
        if (!visitedTypes.add(type.toString() + name)) return
        if (name != ANY) entry(name)
        addEnclosingClasses(declaration)
        type.arguments.forEach { argument -> argument.type?.resolve()?.let(::addTypeTree) }
    }

    /** JVM parameter types of a constructor, as Java type names (`int`, `java.util.List`, `java.lang.String[]`). */
    private fun KSFunctionDeclaration.jvmParameterTypes(): List<String>? {
        val signature = resolver.mapToJvmSignature(this)
        if (signature == null) {
            logger.warn("Native metadata: no JVM signature for ${qualifiedName?.asString()}, skipping it.")
            return null
        }
        return JvmDescriptor.parameterTypes(signature)
    }

    private companion object {
        const val OUTPUT_DIR = "META-INF/native-image/eu.vendeli.tgbot.generated/ktgram"
        const val SERVICES_FILE = "META-INF/services/eu.vendeli.tgbot.interfaces.helper.ContextLoader"
        const val ANY = "kotlin.Any"
        val DEFAULTS = setOf(DefaultGuard::class.fqName, DefaultArgParser::class.fqName, DefaultFilter::class.fqName)
    }
}

/** JVM binary name, e.g. `org.example.Outer$Inner`. */
internal fun KSClassDeclaration.binaryName(): String {
    val names = generateSequence(this) { it.parentDeclaration as? KSClassDeclaration }
        .map { it.simpleName.asString() }
        .toList()
        .asReversed()
    val pkg = packageName.asString()
    return if (pkg.isEmpty()) names.joinToString("$") else "$pkg.${names.joinToString("$")}"
}

/** Parses parameter types from JVM method descriptors such as `(Ljava/lang/String;[II)V`. */
internal object JvmDescriptor {
    private val primitives = mapOf(
        'Z' to "boolean",
        'B' to "byte",
        'C' to "char",
        'S' to "short",
        'I' to "int",
        'J' to "long",
        'F' to "float",
        'D' to "double",
    )

    fun parameterTypes(descriptor: String): List<String> {
        val params = descriptor.substringAfter('(').substringBefore(')')
        val types = mutableListOf<String>()
        var i = 0
        while (i < params.length) {
            var dims = 0
            while (params[i] == '[') {
                dims++
                i++
            }
            val base = if (params[i] == 'L') {
                val end = params.indexOf(';', i)
                params.substring(i + 1, end).replace('/', '.').also { i = end + 1 }
            } else {
                requireNotNull(primitives[params[i]]) { "Unknown descriptor type '${params[i]}' in $descriptor" }
                    .also { i++ }
            }
            types += base + "[]".repeat(dims)
        }
        return types
    }
}

/** Minimal deterministic JSON writer for maps, lists and strings. */
internal object MetadataJson {
    fun render(value: Any?, indent: String = ""): String = when (value) {
        is Map<*, *> -> if (value.isEmpty()) {
            "{}"
        } else {
            value.entries.joinToString(",\n", "{\n", "\n$indent}") { (k, v) ->
                "$indent  ${quote(k.toString())}: ${render(v, "$indent  ")}"
            }
        }

        is List<*> -> if (value.isEmpty()) {
            "[]"
        } else {
            value.joinToString(",\n", "[\n", "\n$indent]") { "$indent  ${render(it, "$indent  ")}" }
        }

        is String -> quote(value)

        else -> error("Unsupported JSON value: $value")
    }

    private fun quote(s: String) = buildString {
        append('"')
        s.forEach { c ->
            when (c) {
                '"' -> append("\\\"")
                '\\' -> append("\\\\")
                '\n' -> append("\\n")
                else -> append(c)
            }
        }
        append('"')
    }
}
