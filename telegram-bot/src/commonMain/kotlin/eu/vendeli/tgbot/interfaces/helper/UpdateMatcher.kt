package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * User-defined matching logic for a custom condition annotation, see [eu.vendeli.tgbot.annotations.ConditionOf].
 *
 * Implementations must be Kotlin `object`s or classes with a no-arg constructor.
 *
 * @param A the condition annotation whose arguments parameterize this matcher.
 */
interface UpdateMatcher<A : Annotation> {
    /**
     * @param annotation the annotation instance as written at the handler (carries the user's arguments).
     * @return `true` if the handler annotated with [annotation] should process [update].
     */
    suspend fun matches(annotation: A, update: ProcessedUpdate, bot: TelegramBot): Boolean

    /**
     * Binds this matcher to [annotation], producing an [UpdateCondition]. Used by generated code and the DSL.
     */
    fun bind(annotation: A): UpdateCondition =
        UpdateCondition { update, bot -> matches(annotation, update, bot) }
}

/**
 * Runtime form of a condition: an [UpdateMatcher] already bound to its annotation arguments.
 */
fun interface UpdateCondition {
    suspend fun test(update: ProcessedUpdate, bot: TelegramBot): Boolean
}
