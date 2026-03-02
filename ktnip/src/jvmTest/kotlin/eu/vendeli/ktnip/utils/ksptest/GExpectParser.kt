package eu.vendeli.ktnip.utils.ksptest

import eu.vendeli.ktnip.utils.ksptest.models.ErrorExpectation
import eu.vendeli.ktnip.utils.ksptest.models.FileExpectation
import eu.vendeli.ktnip.utils.ksptest.models.GExpectBlock

/**
 * Parses `/* G-EXPECT ... */` block comments from source.
 * Supports file=, contains=, notContains=, matches=, notMatches=, error directives, noError.
 * Multiple file= sections and checks can appear in one block.
 * matches/notMatches use regex for exact pattern matching.
 */
object GExpectParser {

    fun parse(source: String): GExpectBlock? {
        val blockRegex = Regex("""/\*\s*G-EXPECT\s*([\s\S]*?)\*/""")
        val match = blockRegex.find(source) ?: return null

        val body = match.groupValues[1].trim()
        val fileRegex = Regex("""file=(\S+)""")
        val containsRegex = Regex("""contains="([^"]*)"""")
        val notContainsRegex = Regex("""notContains="([^"]*)"""")
        val matchesRegex = Regex("""matches="([^"]*)"""")
        val notMatchesRegex = Regex("""notMatches="([^"]*)"""")
        val errorContainsRegex = Regex("""error\s+contains="([^"]*)" """)
        val errorExactRegex = Regex("""error\s+exact="([^"]*)" """)
        val errorCountRegex = Regex("""error\s+count=(\d+)""")
        val errorFileLineContainsRegex = Regex("""error\s+file=(\S+)\s+line=(\d+)\s+contains="([^"]*)" """)

        val fileExpectations = mutableListOf<FileExpectation>()
        val errorExpectations = mutableListOf<ErrorExpectation>()
        var noError = false

        var currentFile: String? = null
        val currentContains = mutableListOf<String>()
        val currentNotContains = mutableListOf<String>()
        val currentMatches = mutableListOf<String>()
        val currentNotMatches = mutableListOf<String>()

        fun flushFileExpectation() {
            if (currentFile != null) {
                fileExpectations.add(
                    FileExpectation(
                        file = currentFile!!,
                        contains = currentContains.toList(),
                        notContains = currentNotContains.toList(),
                        matches = currentMatches.toList(),
                        notMatches = currentNotMatches.toList(),
                    ),
                )
                currentContains.clear()
                currentNotContains.clear()
                currentMatches.clear()
                currentNotMatches.clear()
                currentFile = null
            }
        }

        for (line in body.lines()) {
            val trimmed = line.trim()
            when {
                fileRegex.matches(trimmed) -> {
                    flushFileExpectation()
                    currentFile = fileRegex.find(trimmed)!!.groupValues[1]
                }

                containsRegex.containsMatchIn(trimmed) -> {
                    containsRegex.find(trimmed)?.groupValues?.get(1)?.takeIf { it.isNotEmpty() }
                        ?.let { currentContains.add(it) }
                }

                notContainsRegex.containsMatchIn(trimmed) -> {
                    notContainsRegex.find(trimmed)?.groupValues?.get(1)?.takeIf { it.isNotEmpty() }
                        ?.let { currentNotContains.add(it) }
                }

                matchesRegex.containsMatchIn(trimmed) -> {
                    matchesRegex.find(trimmed)?.groupValues?.get(1)?.takeIf { it.isNotEmpty() }
                        ?.let { currentMatches.add(it) }
                }

                notMatchesRegex.containsMatchIn(trimmed) -> {
                    notMatchesRegex.find(trimmed)?.groupValues?.get(1)?.takeIf { it.isNotEmpty() }
                        ?.let { currentNotMatches.add(it) }
                }

                trimmed == "noError" -> {
                    flushFileExpectation()
                    noError = true
                }

                errorContainsRegex.containsMatchIn(trimmed) -> {
                    flushFileExpectation()
                    errorExpectations.add(ErrorExpectation(contains = errorContainsRegex.find(trimmed)!!.groupValues[1]))
                }

                errorExactRegex.containsMatchIn(trimmed) -> {
                    flushFileExpectation()
                    errorExpectations.add(ErrorExpectation(exact = errorExactRegex.find(trimmed)!!.groupValues[1]))
                }

                errorCountRegex.containsMatchIn(trimmed) -> {
                    flushFileExpectation()
                    errorExpectations.add(ErrorExpectation(count = errorCountRegex.find(trimmed)!!.groupValues[1].toInt()))
                }

                errorFileLineContainsRegex.containsMatchIn(trimmed) -> {
                    flushFileExpectation()
                    val m = errorFileLineContainsRegex.find(trimmed)!!
                    errorExpectations.add(
                        ErrorExpectation(
                            file = m.groupValues[1],
                            line = m.groupValues[2].toInt(),
                            messageContains = m.groupValues[3],
                        ),
                    )
                }
            }
        }
        flushFileExpectation()

        return GExpectBlock(
            fileExpectations = fileExpectations,
            errorExpectations = errorExpectations,
            noError = noError,
        )
    }
}
