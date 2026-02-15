package eu.vendeli.ktnip.utils.ksptest.models

data class FileExpectation(
    val file: String,
    val contains: List<String>,
    val notContains: List<String>,
    val matches: List<String>,
    val notMatches: List<String>,
)
