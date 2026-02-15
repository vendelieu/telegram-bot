package eu.vendeli.ktnip.utils.ksptest.models

data class GExpectBlock(
    val fileExpectations: List<FileExpectation>,
    val errorExpectations: List<ErrorExpectation>,
    val noError: Boolean,
)
