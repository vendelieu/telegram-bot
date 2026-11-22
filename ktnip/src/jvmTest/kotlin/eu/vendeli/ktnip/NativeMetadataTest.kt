@file:OptIn(ExperimentalCompilerApi::class)

package eu.vendeli.ktnip

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspProcessorOptions
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import eu.vendeli.ktnip.codegen.JvmDescriptor
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import java.io.File

/**
 * Checks for the GraalVM metadata ktnip generates, beyond the golden files of ProcessorTest.
 */
class NativeMetadataTest : AnnotationSpec() {
    @Test
    fun `descriptor parser maps every JVM type`() {
        JvmDescriptor.parameterTypes("(ZBCSIJFDLjava/lang/String;[I[[Ljava/util/List;)V") shouldBe listOf(
            "boolean",
            "byte",
            "char",
            "short",
            "int",
            "long",
            "float",
            "double",
            "java.lang.String",
            "int[]",
            "java.util.List[][]",
        )
        JvmDescriptor.parameterTypes("()V") shouldBe emptyList()
    }

    @Test
    fun `every loader in the services file is registered, with multiple packages and Java classes`() {
        val kspDir = compile(
            SourceFile.kotlin(
                "One.kt",
                """
                package a.one

                import eu.vendeli.tgbot.annotations.ArgParser
                import eu.vendeli.tgbot.annotations.CommandHandler
                import eu.vendeli.tgbot.types.component.UpdateType

                class OneHandler {
                    @CommandHandler(["/one"], scope = [UpdateType.MESSAGE])
                    @ArgParser(JavaParser::class)
                    suspend fun one() {}
                }
                """.trimIndent(),
            ),
            SourceFile.java(
                "JavaParser.java",
                """
                package a.one;

                import java.util.Map;
                import eu.vendeli.tgbot.interfaces.helper.ArgumentParser;

                public class JavaParser implements ArgumentParser {
                    @Override
                    public Map<String, String> parse(String text) { return Map.of(); }
                }
                """.trimIndent(),
            ),
            SourceFile.kotlin(
                "Two.kt",
                """
                package a.two

                import eu.vendeli.tgbot.annotations.CommandHandler
                import eu.vendeli.tgbot.types.component.UpdateType

                class TwoHandler {
                    @CommandHandler(["/two"], scope = [UpdateType.MESSAGE])
                    suspend fun two() {}
                }
                """.trimIndent(),
            ),
            options = mapOf("package" to "a.one;a.two"),
        )

        val loaders = kspDir
            .resolveUnique("eu.vendeli.tgbot.interfaces.helper.ContextLoader")
            .readLines()
            .filter { it.isNotBlank() }
        val metadata = kspDir.resolveUnique("reachability-metadata.json").readText()

        loaders.size shouldBe 2
        // Every ServiceLoader provider, and every class created through ClassManager, needs its no-arg constructor.
        (loaders + listOf("a.one.OneHandler", "a.one.JavaParser", "a.two.TwoHandler"))
            .filterNot { metadata.containsNoArgConstructor(it) }
            .shouldBeEmpty()
        metadata.lines().map { it.trim() } shouldContainAll listOf(
            "\"glob\": \"META-INF/services/eu.vendeli.tgbot.interfaces.helper.ContextLoader\"",
        )
    }

    private fun compile(vararg sources: SourceFile, options: Map<String, String>): File {
        val compilation = KotlinCompilation().apply {
            useKsp2()
            jvmTarget = "17"
            languageVersion = "2.2"
            this.sources = sources.asList()
            symbolProcessorProviders = mutableListOf(ActivityProcessorProvider())
            kspProcessorOptions = options.toMutableMap()
            inheritClassPath = true
            verbose = false
        }
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.OK
        return compilation.kspSourcesDir
    }

    private fun File.resolveUnique(name: String): File =
        walkTopDown().filter { it.isFile && it.name == name }.toList().single()

    /** Whether the JSON registers [type] with a `<init>` taking no parameters. */
    private fun String.containsNoArgConstructor(type: String): Boolean = Regex(
        "\"type\": \"${Regex.escape(type)}\",\\s*\"methods\": \\[\\s*\\{\\s*\"name\": \"<init>\",\\s*" +
            "\"parameterTypes\": \\[]",
    ).containsMatchIn(this)
}
