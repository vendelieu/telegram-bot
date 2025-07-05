package eu.vendeli.tgbot.utils.internal

import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.types.component.LogLvl
import eu.vendeli.tgbot.utils.common.safeCast
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.Logger

internal actual inline fun getLogger(lvl: LogLvl, tag: String): Logger = KtorSimpleLogger(tag).apply {
    runCatching { safeCast<ch.qos.logback.classic.Logger>()?.level = Level.valueOf(lvl.name) }
}
