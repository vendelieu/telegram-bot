package eu.vendeli.aide.dto

internal data class SourceKey(
    /**
     * The starting offset of the element in the source file.
     */
    val startOffset: Int,
    /**
     * The ending offset of the element in the source file.
     */
    val endOffset: Int,
)
