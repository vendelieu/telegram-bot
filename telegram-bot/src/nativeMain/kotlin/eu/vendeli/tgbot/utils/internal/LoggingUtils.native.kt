package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.types.component.LogLvl
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.Logger

internal actual inline fun getLogger(
    lvl: LogLvl,
    tag: String,
): Logger = KtorSimpleLogger(tag)
