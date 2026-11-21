import groovy.json.JsonOutput
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * Registers [GenerateNativeImageMetadata] for the JVM target and packs its output into the JVM jar.
 * Call after `configuredKotlin { }`, which applies the Kotlin Multiplatform plugin.
 */
fun Project.nativeImageMetadata() {
    val kotlin = extensions.getByType<KotlinMultiplatformExtension>()

    val generate = tasks.register<GenerateNativeImageMetadata>("generateNativeImageMetadata") {
        group = "build"
        description = "Generates GraalVM reflection metadata for kotlinx.serialization runtime lookups"
        classesDirs.from(kotlin.jvm().compilations.getByName("main").output.classesDirs)
        metadataPath.set(provider { "${project.group}/${project.name}" })
        outputDir.set(layout.buildDirectory.dir("generated/native-image"))
    }

    tasks.named<Jar>("jvmJar") { from(generate) }
    // Lets NativeImageMetadataTest read the generated files from the test classpath.
    kotlin.sourceSets.named("jvmTest") { resources.srcDir(generate) }
}

/**
 * Generates GraalVM reflection metadata for what kotlinx.serialization looks up reflectively
 * on the JVM when the serializer is resolved at runtime (`KClass.serializer()`, `serializer(KType)`):
 *  - rule 1: the static `Companion` field and `Companion.serializer(...)`
 *  - rule 2: `INSTANCE` and `serializer()` of a serializable `object`
 *  - rule 3: enums without a generated serializer, which kotlinx builds from `Class.getEnumConstants()`
 *
 * Serialization itself (generated `$serializer` code) needs no reflection.
 */
abstract class GenerateNativeImageMetadata : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val classesDirs: ConfigurableFileCollection

    /** Path under META-INF/native-image, by convention `<group>/<artifact>`. */
    @get:Input
    abstract val metadataPath: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        // Read every class file once: no class loading, no runtime classpath needed.
        val classes = classesDirs.asFileTree
            .matching { include("**/*.class") }
            .files
            .map { file -> ClassShape.read(file.readBytes()) }
            .associateBy { it.name }

        val registrations = mutableListOf<Registration>()
        val unsupported = mutableListOf<String>()

        for (cls in classes.values) {
            // Rule 1: `Companion` field whose class declares serializer(...) returning KSerializer.
            var hasCompanionSerializer = false
            cls.staticFields["Companion"]?.let { desc ->
                val companion = classes[Type.getType(desc).className] ?: return@let
                val overloads = companion.serializerOverloads()
                if (overloads.isNotEmpty()) {
                    hasCompanionSerializer = true
                    registrations += Registration(cls.name, fields = listOf("Companion"))
                    registrations += Registration(companion.name, methods = overloads.map { "serializer" to it })
                    // Generic class: no no-arg serializer(), so a KClass lookup falls through to the
                    // `$serializer` probe. Registering the type keeps the "not found" result the JVM gives.
                    val generated = classes[cls.name + "\$\$serializer"]
                    if (overloads.none { it.isEmpty() } && generated != null) {
                        registrations += Registration(generated.name)
                    }
                }
            }

            // Rule 2: serializable object, i.e. static INSTANCE of its own type plus a no-arg serializer().
            if (cls.staticFields["INSTANCE"] == cls.descriptor && cls.serializerOverloads().any { it.isEmpty() }) {
                registrations += Registration(
                    cls.name,
                    fields = listOf("INSTANCE"),
                    methods = listOf("serializer" to emptyList()),
                )
            }

            // Rule 3: enum without `@Serializable`; kotlinx falls back to an EnumSerializer built from
            // Class.getEnumConstants(), which calls the enum's static values() reflectively.
            if (cls.isEnum && !hasCompanionSerializer) {
                registrations += Registration(cls.name, methods = listOf("values" to emptyList()))
            }

            // Named companions (`companion object Factory`) carry @NamedCompanion and are found through
            // getDeclaredFields() + annotations, which these rules don't register. Fail the build instead of
            // failing at runtime in a native image.
            if (ClassShape.NAMED_COMPANION in cls.annotations) unsupported += cls.name
        }

        if (unsupported.isNotEmpty()) {
            throw GradleException(
                "Named companion objects of serializable classes are not supported by $name: " +
                    unsupported.sorted().joinToString() +
                    ". Rename them to `companion object` or extend GenerateNativeImageMetadata.",
            )
        }

        val sorted = registrations.distinct().sortedBy { it.type }

        val root = outputDir.get().asFile.apply { deleteRecursively() }
        val dir = root.resolve("META-INF/native-image/${metadataPath.get()}").apply { mkdirs() }

        // Single-file format, read by current GraalVM releases.
        dir.resolve("reachability-metadata.json").writeText(
            JsonOutput.prettyPrint(
                JsonOutput.toJson(mapOf("reflection" to sorted.map { it.toJson(typeKey = "type") })),
            ) + "\n",
        )
        // Legacy format, read by older GraalVM / Mandrel releases.
        dir.resolve("reflect-config.json").writeText(
            JsonOutput.prettyPrint(JsonOutput.toJson(sorted.map { it.toJson(typeKey = "name") })) + "\n",
        )

        logger.lifecycle("Native image metadata: ${sorted.size} registrations from ${classes.size} classes")
    }
}

private data class Registration(
    val type: String,
    val fields: List<String> = emptyList(),
    /** Method name to parameter types. */
    val methods: List<Pair<String, List<String>>> = emptyList(),
) {
    fun toJson(typeKey: String): Map<String, Any> = buildMap {
        put(typeKey, type)
        if (fields.isNotEmpty()) put("fields", fields.map { mapOf("name" to it) })
        if (methods.isNotEmpty()) {
            put("methods", methods.map { (name, params) -> mapOf("name" to name, "parameterTypes" to params) })
        }
    }
}

/** The parts of a compiled class the rules need. */
private class ClassShape private constructor() : ClassVisitor(Opcodes.ASM9) {
    /** Binary name, e.g. `eu.vendeli.tgbot.types.Foo$Bar`. */
    var name = ""
        private set

    /** JVM descriptor of the class itself, e.g. `Leu/vendeli/tgbot/types/Foo$Bar;`. */
    var descriptor = ""
        private set

    /** Whether the class is an enum. */
    var isEnum = false
        private set

    /** Descriptors of the class's runtime-visible annotations. */
    val annotations = mutableSetOf<String>()

    /** Static field name to descriptor. */
    val staticFields = mutableMapOf<String, String>()

    /** Method name to descriptor. */
    private val methods = mutableListOf<Pair<String, String>>()

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?,
    ) {
        this.name = Type.getObjectType(name).className
        this.descriptor = Type.getObjectType(name).descriptor
        this.isEnum = access and Opcodes.ACC_ENUM != 0
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        if (visible) annotations += descriptor
        return null
    }

    override fun visitField(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        value: Any?,
    ): FieldVisitor? {
        if (access and Opcodes.ACC_STATIC != 0) staticFields[name] = descriptor
        return null
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor? {
        methods += name to descriptor
        return null
    }

    /** Parameter types of each `serializer(...)` returning KSerializer; generic classes take KSerializers. */
    fun serializerOverloads(): List<List<String>> = methods
        .filter { (name, desc) -> name == "serializer" && Type.getReturnType(desc).descriptor == KSERIALIZER }
        .map { (_, desc) -> Type.getArgumentTypes(desc).map { it.className } }

    companion object {
        private const val KSERIALIZER = "Lkotlinx/serialization/KSerializer;"
        const val NAMED_COMPANION = "Lkotlinx/serialization/internal/NamedCompanion;"

        fun read(bytes: ByteArray): ClassShape = ClassShape().also {
            ClassReader(bytes).accept(it, ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
        }
    }
}
