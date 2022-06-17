package eu.vendeli.samples

import eu.vendeli.tgbot.types.Update
import org.slf4j.LoggerFactory

object ExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun handleException(update: Update, ex: Throwable) {
        logger.info("Received an exception while processing an update: $update", ex)
    }
}