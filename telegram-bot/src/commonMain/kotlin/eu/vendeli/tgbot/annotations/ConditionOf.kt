package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.interfaces.helper.UpdateMatcher
import kotlin.reflect.KClass

/**
 * Meta-annotation that turns a user-defined annotation into a custom condition for update handlers.
 *
 * Put it on your own annotation class together with [UpdateHandler]; the annotation's arguments are
 * handed to [matcher] at runtime, after the cheap static checks (update type, message kind) have passed.
 * When a handler carries several conditions, all of them must match.
 *
 * ```kotlin
 * @ConditionOf(MinPhotoWidthMatcher::class)
 * @UpdateHandler([UpdateType.MESSAGE], messageKind = [MessageKind.PHOTO])
 * annotation class WidePhoto(val minWidth: Int = 0)
 *
 * object MinPhotoWidthMatcher : UpdateMatcher<WidePhoto> { ... }
 *
 * @WidePhoto(minWidth = 800)
 * suspend fun onWidePhoto(update: MessageUpdate, bot: TelegramBot) { ... }
 * ```
 *
 * Supported by [UpdateHandler].
 *
 * @property matcher Matcher type; must implement `UpdateMatcher<ThisAnnotation>`.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ConditionOf(
    val matcher: KClass<out UpdateMatcher<*>>,
)
