package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.interfaces.helper.ExceptionHandler

/**
 * Strategies to do with exceptions and updates that caused them.
 */
sealed class ExceptionHandlingStrategy {
    /**
     * Collect to `TgUpdateHandler.caughtExceptions`.
     */
    data object CollectToChannel : ExceptionHandlingStrategy()

    /**
     * Throw again wrapped with `TgException`.
     */
    data object Throw : ExceptionHandlingStrategy()

    /**
     * Do nothing :).
     */
    data object DoNothing : ExceptionHandlingStrategy()

    /**
     * Set custom handler.
     *
     * @property handler exception handler.
     */
    data class Handle(val handler: ExceptionHandler) : ExceptionHandlingStrategy()
}
