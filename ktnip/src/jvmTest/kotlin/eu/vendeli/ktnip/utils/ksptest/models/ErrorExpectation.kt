package eu.vendeli.ktnip.utils.ksptest.models

data class ErrorExpectation(
    val contains: String? = null,
    val exact: String? = null,
    val count: Int? = null,
    val file: String? = null,
    val line: Int? = null,
    val messageContains: String? = null,
)
