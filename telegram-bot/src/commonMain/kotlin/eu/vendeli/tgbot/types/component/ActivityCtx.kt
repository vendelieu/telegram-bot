package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.annotations.dsl.FunctionalDSL

@FunctionalDSL
data class ActivityCtx<out T : ProcessedUpdate>(
    val update: T,
)
