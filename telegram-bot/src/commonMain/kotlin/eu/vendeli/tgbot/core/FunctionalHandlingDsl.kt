package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.internal.ActivityCtx
import eu.vendeli.tgbot.types.internal.CommonMatcher
import eu.vendeli.tgbot.types.internal.FunctionalActivities
import eu.vendeli.tgbot.types.internal.FunctionalInvocation
import eu.vendeli.tgbot.types.internal.InputBreakPoint
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.SingleInputChain
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.DEFAULT_COMMAND_SCOPE
import eu.vendeli.tgbot.utils.LoggingWrapper
import eu.vendeli.tgbot.utils.OnCommandActivity
import eu.vendeli.tgbot.utils.OnInputActivity
import eu.vendeli.tgbot.utils.WhenNotHandledActivity
import kotlin.reflect.KClass

/**
 * DSL for functional update management.
 *
 * @property bot
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions")
class FunctionalHandlingDsl internal constructor(
    internal val bot: TelegramBot,
) {
    internal val functionalActivities = FunctionalActivities()
    internal val logger = LoggingWrapper(bot.config.logging, "eu.vendeli.FunctionalHandler")

    /**
     * The action that is performed when the command is matched.
     *
     * @param command The command that will be processed.
     * @param scope update type that should match for command.
     * @param rateLimits Restriction of command requests.
     * @param block Action that will be applied.
     */
    fun onCommand(
        command: String,
        scope: Set<UpdateType> = DEFAULT_COMMAND_SCOPE,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        guard: Guard = DefaultGuard,
        argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
        block: OnCommandActivity,
    ) {
        scope.forEach {
            functionalActivities.commands[command to it] =
                FunctionalInvocation(command, block, scope, rateLimits, guard, argParser = argParser)
        }
    }

    /**
     * The action that is performed when the input is matched.
     *
     * @param identifier Input identifier.
     * @param rateLimits Restriction of input requests.
     * @param block Action that will be applied.
     */
    fun onInput(
        identifier: String,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        guard: Guard = DefaultGuard,
        block: OnInputActivity,
    ) {
        functionalActivities.inputs[identifier] = SingleInputChain(identifier, block, rateLimits, guard)
    }

    /**
     * Action that will be applied when none of the other handlers process the data
     */
    fun whenNotHandled(block: WhenNotHandledActivity) {
        functionalActivities.whenNotHandled = block
    }

    /**
     * Common action that will be checked after other activities.
     *
     * @param value value that will be matched.
     * @param filter condition that will be checked in a matching process.
     * @param scope update type that should match for command.
     * @param rateLimits restriction of command requests.
     * @param block action that will be applied.
     */
    fun common(
        value: String,
        filter: KClass<out Filter> = DefaultFilter::class,
        scope: Set<UpdateType> = DEFAULT_COMMAND_SCOPE,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
        block: OnCommandActivity,
    ) {
        functionalActivities.commonActivities[CommonMatcher.String(value, filter, scope)] =
            FunctionalInvocation(value, block, scope, rateLimits, argParser = argParser)
    }

    /**
     * Common action that will be checked after other activities.
     *
     * @param value value that will be matched.
     * @param filter condition that will be checked in a matching process.
     * @param scope update type that should match for command.
     * @param rateLimits restriction of command requests.
     * @param block action that will be applied.
     */
    fun common(
        value: Regex,
        filter: KClass<out Filter> = DefaultFilter::class,
        scope: Set<UpdateType> = DEFAULT_COMMAND_SCOPE,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
        block: OnCommandActivity,
    ) {
        functionalActivities.commonActivities[CommonMatcher.Regex(value, filter, scope)] =
            FunctionalInvocation(value.pattern, block, scope, rateLimits, argParser = argParser)
    }

    /**
     * Dsl for creating a chain of input processing
     *
     * @param identifier id of input
     * @param rateLimits Restriction of input requests.
     * @param block action that will be applied if input matches
     * @return [SingleInputChain] for further chaining
     */
    fun inputChain(
        identifier: String,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        guard: Guard = DefaultGuard,
        block: OnInputActivity,
    ): SingleInputChain {
        val firstChain = SingleInputChain(identifier, block, rateLimits, guard)
        functionalActivities.inputs[identifier] = firstChain

        return firstChain
    }

    /**
     * Adding a chain to the input data processing
     *
     * @param rateLimits Restriction of input requests.
     * @param block action that will be applied if the inputs match the current chain level
     * @return [SingleInputChain] for further chaining
     */
    fun SingleInputChain.andThen(
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        guard: Guard = DefaultGuard,
        block: OnInputActivity,
    ): SingleInputChain {
        val nextLevel = currentLevel + 1
        val newId = if (currentLevel > 0) {
            id.replace(
                "_chain_lvl_$currentLevel",
                "_chain_lvl_$nextLevel",
            )
        } else {
            id + "_chain_lvl_1"
        }

        functionalActivities.inputs[id]?.tail = newId
        functionalActivities.inputs[newId] = SingleInputChain(newId, block, rateLimits, guard, nextLevel)
        return this
    }

    /**
     * Condition, which will cause the chain to be interrupted if it matches.
     *
     */
    fun SingleInputChain.breakIf(
        condition: ActivityCtx<ProcessedUpdate>.() -> Boolean,
        repeat: Boolean = true,
        block: OnInputActivity? = null,
    ): SingleInputChain {
        functionalActivities.inputs[id]?.breakPoint = InputBreakPoint(condition, block, repeat)
        return this
    }
}
