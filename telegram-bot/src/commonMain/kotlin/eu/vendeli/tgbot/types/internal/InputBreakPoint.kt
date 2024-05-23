package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.utils.OnInputActivity

data class InputBreakPoint(
    val condition: ActivityCtx<ProcessedUpdate>.() -> Boolean,
    val activity: OnInputActivity? = null,
    val repeat: Boolean = true,
)
