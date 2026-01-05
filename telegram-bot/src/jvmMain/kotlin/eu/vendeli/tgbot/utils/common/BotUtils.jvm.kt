package eu.vendeli.tgbot.utils.common

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.interfaces.helper.ContextLoader
import eu.vendeli.tgbot.types.component.LogLvl
import eu.vendeli.tgbot.types.configuration.BotConfiguration
import kotlinx.coroutines.Dispatchers
import org.slf4j.LoggerFactory
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

var BotConfiguration.logLevel: LogLvl?
    get() = LoggerFactory.getLogger("eu.vendeli.tgbot")
        .safeCast<Logger>()?.level?.let { LogLvl.valueOf(it.levelStr) }
    set(value) {
        if (value == null) return
        val logger = LoggerFactory.getLogger("eu.vendeli.tgbot").safeCast<Logger>()
        logger?.level = Level.toLevel(value.name, Level.INFO)
    }
