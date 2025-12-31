package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.interfaces.helper.ContextLoader
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.reflect.KClass

internal actual val PROCESSING_DISPATCHER = Dispatchers.IO

@KtGramInternal
actual fun TelegramBot.loadContext(ctx: ContextLoader?) {
    if (ctx != null) {
        ctx.load(this)
        return
    }
    val ctxLoaders = ServiceLoader.load(ContextLoader::class.java)
    val ctxLoader = ctxLoaders.find {
        it.pkg == commandsPackage
    } ?: ctxLoaders.singleOrNull() ?: error("No context loader found (check ksp generated sources)")

    ctxLoader.load(this)
}

actual val KClass<*>.fqName: String
    get() = qualifiedName ?: simpleName ?: "Unknown"
