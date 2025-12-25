package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.utils.common.ProcessingCtxKey
import eu.vendeli.tgbot.utils.common.safeCast

class AdditionalContext internal constructor() {
    internal val state: MutableMap<ProcessingCtxKey, Any?> = mutableMapOf()

    val regexMatch: MatchResult? get() = state[ProcessingCtxKey.REGEX_MATCH].safeCast()

    internal companion object {
        val EMPTY = AdditionalContext()
    }
}
