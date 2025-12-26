package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.types.component.UpdateType
import kotlin.reflect.KClass

/**
 * The annotation used to mark the function used to process the specified commands.
 *
 * Have lower priority than `@CommandHandler`/`@InputHandler`.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class CommonHandler {
    /**
     * Annotation to specify text matching against update.
     *
     * @property value matching value.
     * @property filters conditions used in matching process.
     * @property priority priority of activity. (0 is highest)
     * @property scope scope `UpdateType` in which the command will be checked.
     */
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Text(
        val value: Array<String>,
        val filters: Array<KClass<out Filter>> = [],
        val priority: Int = 0,
        val scope: Array<UpdateType> = [UpdateType.MESSAGE],
    )

    /**
     * Annotation to specify text matching against update.
     *
     * @property value matching value.
     * @property options regex options that will be used in a regex pattern.
     * @property filters conditions used in matching process.
     * @property priority priority of activity. (0 is highest)
     * @property scope scope `UpdateType` in which the command will be checked.
     */
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Regex(
        val value: String,
        val options: Array<RegexOption> = [],
        val filters: Array<KClass<out Filter>> = [],
        val priority: Int = 0,
        val scope: Array<UpdateType> = [UpdateType.MESSAGE],
    )
}
