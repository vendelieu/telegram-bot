package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.utils.common.OnInputActivity

data class InputBreakPoint(
    val condition: ActivityCtx<ProcessedUpdate>.() -> Boolean,
    val activity: OnInputActivity? = null,
    val repeat: Boolean = true,
)
