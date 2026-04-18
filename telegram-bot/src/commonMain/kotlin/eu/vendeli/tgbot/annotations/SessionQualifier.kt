package eu.vendeli.tgbot.annotations

/**
 * Tags a [eu.vendeli.tgbot.interfaces.session.Session] handler parameter so multiple
 * independent sessions can be injected into the same function.
 *
 * ```
 * @CommandHandler(["/wizard"])
 * suspend fun wizard(
 *     @SessionQualifier("wizard") wizard: Session,
 *     @SessionQualifier("support") support: Session,
 *     bot: TelegramBot,
 * ) { … }
 * ```
 *
 * Resolves to `bot.sessions?.of(update, qualifier = "wizard")` at codegen time. Omit the
 * annotation for the default (unqualified) session.
 *
 * @property value Qualifier string stored in [eu.vendeli.tgbot.types.session.SessionKey.qualifier].
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class SessionQualifier(
    val value: String,
)
