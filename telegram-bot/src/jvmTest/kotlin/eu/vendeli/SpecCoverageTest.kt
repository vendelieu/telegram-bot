package eu.vendeli

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.io.File

/**
 * Ratchet over the Bot API spec in `buildSrc/.../api.json`: a method added to the spec without an implemented
 * action fails the build instead of being noticed later. Known gaps must be listed in [KNOWN_UNIMPLEMENTED].
 */
class SpecCoverageTest : AnnotationSpec() {
    private val repoRoot: File by lazy {
        generateSequence(File(System.getProperty("user.dir")).absoluteFile) { it.parentFile }
            .first { File(it, SPEC_PATH).exists() }
    }

    private val specMethods: Set<String> by lazy {
        Json
            .parseToJsonElement(File(repoRoot, SPEC_PATH).readText())
            .jsonObject
            .getValue("methods")
            .jsonObject
            .keys
    }

    private val implementedMethods: Set<String> by lazy {
        File(repoRoot, "telegram-bot/src/commonMain/kotlin")
            .walkTopDown()
            .filter { it.extension == "kt" }
            .flatMap { NAME_REGEX.findAll(it.readText()).map { m -> m.groupValues[1] } }
            .toSet()
    }

    @Test
    fun `spec lists methods`() {
        specMethods.shouldNotBeEmpty()
    }

    @Test
    fun `every spec method has an action`() {
        (specMethods - implementedMethods - KNOWN_UNIMPLEMENTED).sorted().shouldBeEmpty()
    }

    @Test
    fun `known gaps are really missing`() {
        // Keeps the allowlist honest: once a method is implemented it must be removed from the list.
        (KNOWN_UNIMPLEMENTED intersect implementedMethods).shouldBeEmpty()
    }

    private companion object {
        const val SPEC_PATH = "buildSrc/src/main/resources/api.json"
        val NAME_REGEX = Regex("""@TgAPI\.Name\("([^"]+)"\)""")
        val KNOWN_UNIMPLEMENTED = emptySet<String>()
    }
}
