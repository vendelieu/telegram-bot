package eu.vendeli.ksp.dto

import eu.vendeli.tgbot.types.internal.UpdateType

internal data class AnnotationData(
    val value: List<String>,
    val rateLimits: Pair<Long, Long>,
    val scope: List<UpdateType>,
    val guardClass: String,
    val argParserClass: String,
    val isAutoAnswer: Boolean
)
