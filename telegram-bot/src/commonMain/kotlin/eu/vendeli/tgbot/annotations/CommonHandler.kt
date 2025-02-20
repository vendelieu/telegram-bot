package eu.vendeli.tgbot.annotations

import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.types.component.UpdateType
import kotlin.reflect.KClass

/**
 * The annotation used to mark the function used to process the specified commands.
 *
 * Have lower priority than `@CommandHandler`/`@InputHandler`.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CommonHandler {
    /**
     * Annotation to specify text matching against update.
     *
     * @property value matching value.
     * @property filter condition that used in matching process.
     * @property priority priority of activity. (0 is highest)
     * @property scope scope `UpdateType` in which the command will be checked.
     * @property rateLimits query limits for this particular command.
     * @property argParser Custom argument parser.
     */
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Text(
        val value: Array<String>,
        val filter: KClass<out Filter> = DefaultFilter::class,
        val priority: Int = 0,
        val scope: Array<UpdateType> = [UpdateType.MESSAGE],
        @Deprecated(
            "The functionality of the parameter is given in a separate annotation, please use it. The parameter will be removed soon.",
        )
        val rateLimits: RateLimits = RateLimits(0, 0),
        @Deprecated(
            "The functionality of the parameter is given in a separate annotation, please use it. The parameter will be removed soon.",
        )
        val argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
    )

    /**
     * Annotation to specify text matching against update.
     *
     * @property value matching value.
     * @property options regex options that will be used in a regex pattern.
     * @property filter condition that used in matching process.
     * @property priority priority of activity. (0 is highest)
     * @property scope scope `UpdateType` in which the command will be checked.
     * @property rateLimits query limits for this particular command.
     * @property argParser Custom argument parser.
     */
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Regex(
        val value: String,
        val options: Array<RegexOption> = [],
        val filter: KClass<out Filter> = DefaultFilter::class,
        val priority: Int = 0,
        val scope: Array<UpdateType> = [UpdateType.MESSAGE],
        @Deprecated(
            "The functionality of the parameter is given in a separate annotation, please use it. The parameter will be removed soon.",
        )
        val rateLimits: RateLimits = RateLimits(0, 0),
        @Deprecated(
            "The functionality of the parameter is given in a separate annotation, please use it. The parameter will be removed soon.",
        )
        val argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
    )
}
