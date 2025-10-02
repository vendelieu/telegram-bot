package eu.vendeli.ksp.dto

import eu.vendeli.tgbot.types.component.UpdateType

internal data class AnnotationData(
    val value: List<String>,
    val scope: List<UpdateType>,
    val isAutoAnswer: Boolean? = null,
)
