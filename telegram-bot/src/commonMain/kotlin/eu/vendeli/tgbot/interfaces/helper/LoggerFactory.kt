package eu.vendeli.tgbot.interfaces.helper

import io.ktor.util.logging.Logger

fun interface LoggerFactory {
    fun get(name: String): Logger
}
