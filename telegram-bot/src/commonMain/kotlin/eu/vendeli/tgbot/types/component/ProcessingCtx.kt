package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.utils.common.ProcessingCtxKey
import eu.vendeli.tgbot.utils.common.safeCast

class ProcessingCtx internal constructor() {
    internal val state: MutableMap<ProcessingCtxKey, Any?> = mutableMapOf()

    val invocationMeta: InvocationMeta? get() = state[ProcessingCtxKey.INVOCATION_META].safeCast()
    val regexMatch: MatchResult? get() = state[ProcessingCtxKey.REGEX_MATCH].safeCast()
    val parsedParameters: Map<String, String>? get() = state[ProcessingCtxKey.PARSED_PARAMETERS].safeCast()
    val invocationKind: TgInvocationKind? get() = state[ProcessingCtxKey.INVOCATION_KIND].safeCast()

    internal companion object {
        val EMPTY = ProcessingCtx()
    }
}


context(bot: TelegramBot)
internal fun ProcessingCtx.enrich(key: ProcessingCtxKey, value: Any?) {
    if (!bot.config.processingCtxTargets.contains(key)) return
    state[key] = value
}
