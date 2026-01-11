package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.helper.LoggerFactory
import io.ktor.util.logging.*

object DefaultLoggerFactory : LoggerFactory {
    private val loggerCache = mutableMapOf<String, Logger>()

    override fun get(name: String): Logger = loggerCache.getOrPut(name) { KtorSimpleLogger(name) }
}
