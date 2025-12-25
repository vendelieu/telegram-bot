package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.interfaces.helper.ContextLoader
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.KClass

internal actual val PROCESSING_DISPATCHER = Dispatchers.Unconfined

@KtGramInternal
actual fun TelegramBot.loadContext(ctx: ContextLoader?) {
    ctx?.load(this)
}

@Suppress("unused")
actual val KClass<*>.fqName: String get() = simpleName ?: "Unknown"
