package eu.vendeli.ktnip.utils.ksptest

import java.io.File
import java.net.JarURLConnection
import java.nio.file.Paths

/**
 * Handles golden file operations for KSP output comparison.
 *
 * Golden files are stored at: `test-data/golden/<testName>/<generatedFileName>.gold`
 * - When a golden file exists: compare full file content
 * - When updating: write generated content to golden files
 */
object GoldenFileHandler {

    private const val GOLDEN_BASE = "test-data/golden"
    private const val UPDATE_PROPERTY = "ktnip.golden.update"
    private const val OUTPUT_DIR_PROPERTY = "ktnip.golden.outputDir"

    fun isUpdateMode(): Boolean =
        System.getProperty(UPDATE_PROPERTY) == "true" ||
            System.getenv("KTNIP_GOLDEN_UPDATE") == "true"

    fun getGoldenOutputDir(): File? {
        val path = System.getProperty(OUTPUT_DIR_PROPERTY) ?: System.getenv("KTNIP_GOLDEN_OUTPUT_DIR")
        return path?.let { File(it) }?.takeIf { it.exists() || it.parentFile?.exists() == true }
    }

    /**
     * Resource path for a golden file.
     * E.g. testName="DefaultHandlers", fileName="ActivitiesData.kt" -> "test-data/golden/DefaultHandlers/ActivitiesData.kt.gold"
     */
    fun goldenResourcePath(testName: String, fileName: String): String =
        "$GOLDEN_BASE/$testName/$fileName.gold"

    /**
     * Discovers which golden files exist for a test by listing the golden directory.
     * Works with both file: (exploded resources) and jar: (packaged) classpath.
     * Used when G-EXPECT is absent to build expectations from golden files.
     */
    fun discoverGoldenFiles(classLoader: ClassLoader, testName: String): List<String> {
        val prefix = "$GOLDEN_BASE/$testName/"
        val resource = classLoader.getResource(prefix) ?: return emptyList()

        return when (resource.protocol) {
            "file" -> {
                val dir = Paths.get(resource.toURI()).toFile()
                if (dir.isDirectory) {
                    dir.listFiles()
                        ?.filter { it.isFile && it.name.endsWith(".gold") }
                        ?.map { it.name.removeSuffix(".gold") }
                        ?.sorted()
                        ?: emptyList()
                } else {
                    emptyList()
                }
            }
            "jar" -> {
                val connection = resource.openConnection() as? JarURLConnection ?: return emptyList()
                connection.jarFile.use { jar ->
                    jar.entries()
                        .asSequence()
                        .filter { !it.isDirectory && it.name.startsWith(prefix) && it.name.endsWith(".gold") }
                        .map { it.name.removePrefix(prefix).removeSuffix(".gold") }
                        .sorted()
                        .toList()
                }
            }
            else -> emptyList()
        }
    }

    /**
     * Checks if a golden file exists in resources.
     */
    fun hasGoldenFile(classLoader: ClassLoader, testName: String, fileName: String): Boolean =
        classLoader.getResource(goldenResourcePath(testName, fileName)) != null

    /**
     * Loads golden file content from resources.
     */
    fun loadGoldenContent(classLoader: ClassLoader, testName: String, fileName: String): String? =
        classLoader.getResourceAsStream(goldenResourcePath(testName, fileName))
            ?.bufferedReader()
            ?.use { it.readText() }

    /**
     * Writes content to golden file on disk.
     * Creates parent directories if needed.
     */
    fun writeGoldenFile(outputDir: File, testName: String, fileName: String, content: String) {
        val goldenFile = outputDir.resolve("$testName/$fileName.gold")
        goldenFile.parentFile?.mkdirs()
        goldenFile.writeText(content)
    }
}
