package eu.vendeli.ksp

import eu.vendeli.tgbot.types.internal.UpdateType

data class AnnotationData(
    val value: List<String>,
    val rateLimits: Pair<Long, Long>,
    val scope: List<UpdateType>,
    val guardClass: String,
)
