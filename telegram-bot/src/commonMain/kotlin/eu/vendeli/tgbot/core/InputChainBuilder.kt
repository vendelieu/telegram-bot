package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.component.ActivityCtx
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.component.userOrNull
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.OnInputActivity
import kotlin.reflect.KClass

/**
 * Builder for input chains. Collects steps, builds activities on demand.
 */
class InputChainBuilder internal constructor(
    private val rootId: String,
    rootRateLimits: RateLimits,
    rootGuard: KClass<out Guard>,
    rootBlock: OnInputActivity,
) {
    internal data class Step(
        val id: String,
        val rateLimits: RateLimits,
        val guard: KClass<out Guard>,
        val action: OnInputActivity,
        var breakCondition: (suspend ProcessingContext.() -> Boolean)? = null,
        var breakAction: (suspend ProcessingContext.() -> Unit)? = null,
        var repeatOnBreak: Boolean = true,
    )

    internal val steps = mutableListOf<Step>()
    private var stepIndex = 0

    init {
        steps.add(Step(rootId, rootRateLimits, rootGuard, rootBlock))
    }

    fun andThen(
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        guard: KClass<out Guard> = DefaultGuard::class,
        block: OnInputActivity,
    ): InputChainBuilder {
        stepIndex++
        steps.add(Step("$rootId#$stepIndex", rateLimits, guard, block))
        return this
    }

    fun breakIf(
        condition: suspend ProcessingContext.() -> Boolean,
        repeat: Boolean = true,
        block: (suspend ProcessingContext.() -> Unit)? = null,
    ): InputChainBuilder {
        steps.lastOrNull()?.apply {
            breakCondition = condition
            breakAction = block
            repeatOnBreak = repeat
        }
        return this
    }

    /**
     * Build all chain steps into activities and register them.
     */
    internal fun build(registry: ActivityRegistry, bot: TelegramBot) {
        steps.forEachIndexed { idx, step ->
            val nextStepId = steps.getOrNull(idx + 1)?.id

            val activity = LambdaActivity(
                id = "functional:input:${step.id}".hashCode(),
                function = step.id,
                rateLimits = step.rateLimits,
                guardClass = step.guard,
            ) {
                val ctx = ActivityCtx(update)
                val user = update.userOrNull ?: return@LambdaActivity Unit

                if (step.breakCondition?.invoke(this) == true) {
                    step.breakAction?.invoke(this)
                    if (step.repeatOnBreak) {
                        bot.inputListener.set(user.id, step.id)
                    }
                    return@LambdaActivity Unit
                }

                step.action.invoke(ctx)

                nextStepId?.let { bot.inputListener.set(user.id, it) }
            }

            registry.registerActivity(activity)
            registry.registerInput(step.id, activity.id)
        }
    }
}
