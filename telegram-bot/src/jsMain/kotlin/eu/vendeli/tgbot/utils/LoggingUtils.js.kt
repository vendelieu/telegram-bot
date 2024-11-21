package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.internal.LogLvl
import io.ktor.util.logging.KtorSimpleLogger

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun getLogger(lvl: LogLvl, tag: String) = KtorSimpleLogger(tag)
