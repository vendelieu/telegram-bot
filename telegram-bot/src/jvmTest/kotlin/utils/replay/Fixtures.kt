package utils.replay

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.io.File

/** A single recorded HTTP exchange. Exactly one of [json], [text] and [base64] holds the response body. */
@Serializable
data class RecordedExchange(
    val method: String,
    val requestKeys: List<String> = emptyList(),
    val status: Int,
    val contentType: String? = null,
    val json: JsonElement? = null,
    val text: String? = null,
    val base64: String? = null,
)

/** Recorded exchanges of one spec, grouped by test name. */
@Serializable
data class Cassette(
    val tests: Map<String, List<RecordedExchange>> = emptyMap(),
)

object CassetteStore {
    private const val RESOURCE_DIR = "tg-fixtures"
    private const val FIXTURES_DIR_PROPERTY = "tg.fixtures.dir"

    private val json = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
        ignoreUnknownKeys = true
    }

    private fun resourcePath(specName: String) = "$RESOURCE_DIR/$specName.json"

    fun exists(specName: String): Boolean =
        CassetteStore::class.java.classLoader.getResource(resourcePath(specName)) != null

    fun load(specName: String): Cassette? = CassetteStore::class.java.classLoader
        .getResourceAsStream(resourcePath(specName))
        ?.use { json.decodeFromString<Cassette>(it.readBytes().toString(Charsets.UTF_8)) }

    private fun outputFile(specName: String): File {
        val dir = System.getProperty(FIXTURES_DIR_PROPERTY)
            ?: error("System property $FIXTURES_DIR_PROPERTY is required in record mode, use ./gradlew recordFixtures")
        return File(dir, "$specName.json")
    }

    /** Stores [recorded] tests of the spec, keeping previously recorded tests that were not re-run. */
    @Synchronized
    fun merge(specName: String, recorded: Map<String, List<RecordedExchange>>) {
        if (recorded.isEmpty()) return
        val file = outputFile(specName)
        val existing = file
            .takeIf { it.exists() }
            ?.let { json.decodeFromString<Cassette>(it.readText()) }
            ?.tests
            .orEmpty()
        file.parentFile.mkdirs()
        file.writeText(json.encodeToString(Cassette((existing + recorded).toSortedMap())) + "\n")
    }
}
