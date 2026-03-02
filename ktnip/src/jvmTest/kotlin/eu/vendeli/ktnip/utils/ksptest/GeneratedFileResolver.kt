package eu.vendeli.ktnip.utils.ksptest

import java.io.File

/**
 * Resolves generated KSP output files from the compilation output directory.
 *
 * Strategy:
 * 1. Find all files matching the given name (exact or suffix match)
 * 2. Prefer files under a path containing "generated" or "ksp"
 * 3. If multiple matches in generated paths, prefer the one with the longest path
 *    (typically the most specific package structure)
 */
object GeneratedFileResolver {

    /**
     * Resolves a generated file by name from the KSP sources directory.
     *
     * @param fileName Target file name (e.g. "ActivitiesData.kt" or "KtGramCtxLoader.kt")
     * @param kspSourcesDir Root directory of KSP-generated sources
     * @return The resolved file
     * @throws IllegalStateException if the file is not found
     */
    fun resolve(fileName: String, kspSourcesDir: File): File {
        val allFiles = kspSourcesDir.walkTopDown().filter { it.isFile }.toList()
        val candidates = allFiles.filter { file ->
            file.name == fileName || file.name.endsWith(fileName)
        }

        val inGeneratedPath = candidates.filter {
            it.absolutePath.contains("generated", ignoreCase = true) ||
                it.absolutePath.contains("ksp", ignoreCase = true)
        }

        return inGeneratedPath.maxByOrNull { it.absolutePath.length }
            ?: candidates.firstOrNull()
            ?: error("Generated file '$fileName' not found for expectations")
    }
}
