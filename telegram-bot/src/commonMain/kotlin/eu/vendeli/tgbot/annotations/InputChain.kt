package eu.vendeli.tgbot.annotations

/**
 * Label the class that implements the input chain, for [TgUpdateHandler].
 *
 * @property autoClean Flag to clean state automatically after the last step successfully proceeded,
 * last chain update is used to select key.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class InputChain(
    val autoClean: Boolean = false,
)
