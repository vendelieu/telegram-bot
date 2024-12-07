package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.annotations.dsl.FunctionalDSL

@FunctionalDSL
data class ActivityCtx<out T : ProcessedUpdate>(
    val update: T,
)
