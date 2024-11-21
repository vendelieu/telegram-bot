package eu.vendeli.tgbot.utils

import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.types.internal.LogLvl
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.Logger

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun getLogger(lvl: LogLvl, tag: String): Logger = KtorSimpleLogger(tag).apply {
    safeCast<ch.qos.logback.classic.Logger>()?.level = Level.valueOf(lvl.name)
}
