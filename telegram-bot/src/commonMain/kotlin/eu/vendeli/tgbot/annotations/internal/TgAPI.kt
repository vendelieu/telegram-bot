package eu.vendeli.tgbot.annotations.internal

/**
 * Annotation, which marks all Telegram API methods, and all that can be related.
 *
 */
@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
)
annotation class TgAPI {
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.PROPERTY)
    annotation class Method(
        val name: String,
    )
}
