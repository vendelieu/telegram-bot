package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.types.component.LogLvl
import io.ktor.util.logging.KtorSimpleLogger

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun getLogger(lvl: LogLvl, tag: String) = KtorSimpleLogger(tag)
