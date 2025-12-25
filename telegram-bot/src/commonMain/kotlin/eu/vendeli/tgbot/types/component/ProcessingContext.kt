package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.Activity
import eu.vendeli.tgbot.core.ActivityRegistry

class ProcessingContext(
    val update: ProcessedUpdate,
    val bot: TelegramBot,
    val registry: ActivityRegistry,
) {
    val additionalContext: AdditionalContext = AdditionalContext.EMPTY

    /** Parsed command/input text. */
    var parsedInput: String = ""

    /** Parsed parameters. */
    var parameters: Map<String, String> = emptyMap()

    /** Resolved activity to invoke. */
    var activity: Activity? = null

    /** Continue flag. */
    var shouldProceed: Boolean = true

    fun finish() {
        shouldProceed = false
    }
}
