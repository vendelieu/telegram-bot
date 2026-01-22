package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.dsl.FunctionalDSL
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.component.*
import eu.vendeli.tgbot.types.component.CommonMatcher.Companion.DEFAULT_FILTERS
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.*
import kotlin.reflect.KClass

@Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions")
@FunctionalDSL
class FunctionalHandlingDsl internal constructor(
    internal val bot: TelegramBot,
    internal val registry: ActivityRegistry = bot.update.registry,
) {
    internal val logger = bot.config.loggerFactory.get(this::class.fqName)

    private val pendingChains = mutableListOf<InputChainBuilder>()

    // ===== Commands =====

    fun onCommand(
        command: String,
        scope: Set<UpdateType> = DEFAULT_SCOPE,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        guard: KClass<out Guard> = DefaultGuard::class,
        argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
        block: OnCommandActivity,
    ) {
        val activity = LambdaActivity(
            id = "functional:cmd:$command".hashCode(),
            function = command,
            rateLimits = rateLimits,
            guardClass = guard,
            argParser = argParser,
        ) {
            val cmdCtx = CommandContext(update, parameters)
            block.invoke(cmdCtx)
        }

        registry.registerActivity(activity)
        scope.forEach { type ->
            registry.registerCommand(command, type, activity.id)
        }
    }

    // ===== Inputs =====

    fun onInput(
        identifier: String,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        guard: KClass<out Guard> = DefaultGuard::class,
        block: OnInputActivity,
    ) {
        val activity = LambdaActivity(
            id = "functional:input:$identifier".hashCode(),
            function = identifier,
            rateLimits = rateLimits,
            guardClass = guard,
        ) {
            val ctx = ActivityCtx(update)
            block.invoke(ctx)
        }

        registry.registerActivity(activity)
        registry.registerInput(identifier, activity.id)
    }

    // ===== Input Chains =====

    fun inputChain(
        identifier: String,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        guard: KClass<out Guard> = DefaultGuard::class,
        block: OnInputActivity,
    ): InputChainBuilder {
        val builder = InputChainBuilder(identifier, rateLimits, guard, block)
        pendingChains.add(builder)
        return builder
    }

    // ===== Common Handlers =====

    fun common(
        value: String,
        filters: Set<KClass<out Filter>> = DEFAULT_FILTERS,
        scope: Set<UpdateType> = DEFAULT_SCOPE,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
        block: OnCommandActivity,
    ) {
        val activity = LambdaActivity(
            id = "functional:common:$value".hashCode(),
            function = value,
            rateLimits = rateLimits,
            argParser = argParser,
        ) {
            val cmdCtx = CommandContext(update, parameters)
            block.invoke(cmdCtx)
        }

        registry.registerActivity(activity)
        val matcher = CommonMatcher.String(value, filters, scope)
        scope.forEach { type ->
            registry.registerCommonHandler(matcher, type, activity.id)
        }
    }

    fun common(
        value: Regex,
        filters: Set<KClass<out Filter>> = DEFAULT_FILTERS,
        scope: Set<UpdateType> = DEFAULT_SCOPE,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        argParser: KClass<out ArgumentParser> = DefaultArgParser::class,
        block: OnCommandActivity,
    ) {
        val activity = LambdaActivity(
            id = "functional:common:${value.pattern}".hashCode(),
            function = value.pattern,
            rateLimits = rateLimits,
            argParser = argParser,
        ) {
            val cmdCtx = CommandContext(update, parameters)
            block.invoke(cmdCtx)
        }

        registry.registerActivity(activity)
        val matcher = CommonMatcher.Regex(value, filters, scope)
        scope.forEach { type ->
            registry.registerCommonHandler(matcher, type, activity.id)
        }
    }

    // ===== Update Type Handlers =====

    fun onUpdate(
        vararg types: UpdateType,
        block: suspend ActivityCtx<ProcessedUpdate>.() -> Unit,
    ) {
        types.forEach { type ->
            val activity = LambdaActivity(
                id = "functional:update:${type.name}".hashCode(),
                function = "onUpdate:${type.name}",
            ) {
                val ctx = ActivityCtx(update)
                block.invoke(ctx)
            }

            registry.registerActivity(activity)
            registry.registerUpdateTypeHandler(type, activity.id)
        }
    }

    // ===== Fallback =====

    fun whenNotHandled(block: WhenNotHandledActivity) {
        val activity = LambdaActivity(
            id = "functional:unprocessed".hashCode(),
            function = "whenNotHandled",
        ) {
            val ctx = ActivityCtx(update)
            block.invoke(ctx)
        }

        registry.registerActivity(activity)
        registry.registerUnprocessed(activity.id)
    }

    // ===== Finalize =====

    internal fun apply() {
        pendingChains.forEach { it.build(registry, bot) }
        pendingChains.clear()
    }
}
