package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.ArgumentParser
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.types.chain.SingleInputChain
import eu.vendeli.tgbot.types.component.ActivityCtx
import eu.vendeli.tgbot.types.component.CommandContext
import eu.vendeli.tgbot.types.component.ParsedText
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.component.userOrNull
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.common.cast
import eu.vendeli.tgbot.utils.common.checkIsLimited
import eu.vendeli.tgbot.utils.common.defaultArgParser
import eu.vendeli.tgbot.utils.common.fqName
import eu.vendeli.tgbot.utils.common.handleFailure
import eu.vendeli.tgbot.utils.common.parseCommand
import kotlin.reflect.KClass

private inline val SingleInputChain.prevChainId: String?
    get() = if (currentLevel == 1) {
        id.replace("_chain_lvl_$currentLevel", "")
    } else if (currentLevel > 1) {
        id.replace("_chain_lvl_$currentLevel", "_chain_lvl_${currentLevel - 1}")
    } else {
        null
    }

internal suspend inline fun KClass<out Guard>.checkIsGuarded(
    user: User?,
    update: ProcessedUpdate,
    bot: TelegramBot,
): Boolean {
    if (fqName == DefaultGuard::class.fqName) return true
    return bot.config.classManager
        .getInstance(this)
        .cast<Guard>()
        .condition(user, update, bot)
}

internal suspend inline fun Guard.checkIsGuarded(
    user: User?,
    update: ProcessedUpdate,
    bot: TelegramBot,
): Boolean = if (this::class.fqName == DefaultGuard::class.fqName) true else condition(user, update, bot)

internal suspend inline fun KClass<out Filter>.checkIsNotFiltered(
    user: User?,
    update: ProcessedUpdate,
    bot: TelegramBot,
): Boolean {
    if (fqName == DefaultFilter::class.fqName) return true
    return bot.config.classManager
        .getInstance(this)
        .cast<Filter>()
        .checkIsNotFiltered(user, update, bot)
}

internal suspend inline fun Filter.checkIsNotFiltered(
    user: User?,
    update: ProcessedUpdate,
    bot: TelegramBot,
): Boolean = match(user, update, bot)

internal fun TgUpdateHandler.getParameters(
    parser: KClass<out ArgumentParser>?,
    request: ParsedText,
): Map<String, String> = parser
    ?.let {
        if (it.fqName == DefaultArgParser::class.fqName) bot.config.commandParsing.run {
            defaultArgParser(
                request.tail,
                parametersDelimiter,
                parameterValueDelimiter,
            )
        }
        else bot.config.classManager
            .getInstance(it)
            .cast<ArgumentParser>()
            .parse(request.tail)
    } ?: emptyMap()

/**
 * Method that tries to find activity in given text and invoke it.
 *
 * @param update
 */
@Suppress("CyclomaticComplexMethod", "NestedBlockDepth", "ReturnCount")
private suspend fun FunctionalHandlingDsl.checkMessageForActivities(update: ProcessedUpdate): Boolean = update.run {
    // parse text to chosen format
    val parsedText = bot.update.parseCommand(text)
    logger.debug { "Parsed text - $text to $parsedText" }
    val user = userOrNull

    // find activity that matches command and invoke it
    functionalActivities.commands[parsedText.command to type]?.run {
        logger.debug { "Matched command ${parsedText.command} for text $text" }
        if (user != null) bot.inputListener.del(user.id) // clean input listener
        // check guard condition
        if (!guard.checkIsGuarded(user, update, bot)) {
            logger.debug { "Invocation guarded: $this" }
            return false
        }
        // check for limit exceed
        if (bot.update.checkIsLimited(rateLimits, user?.id, parsedText.command)) return false
        logger.debug { "Invoking command $id" }
        val cmdCtx = CommandContext(update, bot.update.getParameters(argParser, parsedText))
        invocation.invoke(cmdCtx)
        return true
    }
    // if there's no command -> then try process input
    if (user != null) bot.inputListener.get(user.id)?.also {
        logger.info { "Found inputListener point $it for ${user.id}" }

        // search matching input handler for listening point
        val foundChain = functionalActivities.inputs[it]
        if (foundChain != null) {
            // check guard condition
            if (!foundChain.guard.checkIsGuarded(user, update, bot)) {
                logger.debug { "Invocation guarded: $foundChain" }
                return false
            }

            // check for limit exceed
            if (bot.update.checkIsLimited(foundChain.rateLimits, user.id, foundChain.id)) return false

            val inputContext = ActivityCtx(this) // form context for future usage
            val prevChain = foundChain.prevChainId?.let { id -> functionalActivities.inputs[id] }
            // check is there prev chain to check break condition for current lvl
            val isBreakCase = prevChain?.breakPoint?.condition?.invoke(inputContext) == true

            if (!isBreakCase) {
                foundChain.inputActivity.invoke(inputContext)
                // invoke chain if break condition is false
            } else {
                prevChain.breakPoint?.activity?.invoke(inputContext)
                // invoke break point activity when it's a break case
            }

            if (isBreakCase && prevChain.breakPoint?.repeat == true) {
                // and if we need to repeat, do set listener again
                bot.inputListener.set(user.id, foundChain.id)
                return true
            }

            if (!isBreakCase && foundChain.tail != null)
                bot.inputListener.set(user.id, foundChain.tail!!)

            return true
        }
    }
    if (user != null && bot.config.inputAutoRemoval) bot.inputListener.del(user.id) // clean listener

    // if there's no command and input > check common handlers
    functionalActivities.commonActivities.entries
        .firstOrNull { i ->
            i.key.match(parsedText.command, update, bot)
        }?.value
        ?.run {
            logger.debug { "Matched common handler $this for text $text" }
            // check for limit exceed
            if (bot.update.checkIsLimited(rateLimits, user?.id, parsedText.command)) return false
            logger.debug { "Invoking command $id" }
            val cmdCtx = CommandContext(update, bot.update.getParameters(argParser, parsedText))
            invocation.invoke(cmdCtx)
            return true
        }

    return false
}

private suspend fun ((suspend ActivityCtx<ProcessedUpdate>.() -> Unit)?).invokeActivity(
    functionalHandler: FunctionalHandlingDsl,
    updateType: UpdateType,
    activityCtx: ActivityCtx<ProcessedUpdate>,
): Boolean {
    this
        ?.runCatching { invoke(activityCtx) }
        ?.onFailure {
            functionalHandler.logger.error(it) {
                "An error occurred while functionally processing update: ${activityCtx.update} to UpdateType($updateType)."
            }
            functionalHandler.bot.update.handleFailure(activityCtx.update, it)
        }?.onSuccess {
            functionalHandler.logger.info {
                "Update #${activityCtx.update.updateId} processed in functional mode with UpdateType($updateType) activity."
            }
            return true
        }
    return false
}

private inline fun Boolean?.ifAffected(block: () -> Unit) {
    if (this == true) block()
}

/**
 * Process update by functional defined activities.
 *
 * @param update
 */
@Suppress("CyclomaticComplexMethod", "LongMethod")
internal suspend fun FunctionalHandlingDsl.process(update: ProcessedUpdate) = with(update) {
    logger.info { "Handling update #${update.updateId}" }
    var affectedActivities = 0
    if (bot.update.checkIsLimited(bot.config.rateLimiter.limits, userOrNull?.id)) return@with
    val activityCtx = ActivityCtx(this)

    checkMessageForActivities(this).ifAffected { affectedActivities += 1 }
    functionalActivities.onUpdateActivities[type]
        ?.invokeActivity(this@process, type, activityCtx)
        .ifAffected { affectedActivities += 1 }

    if (affectedActivities == 0) functionalActivities.whenNotHandled?.invoke(activityCtx)?.also {
        logger.debug { "Update #${update.updateId} processed in functional mode with whenNotHandled activity." }
        affectedActivities += 1
    }

    logger.debug { "Number of affected functional activities - $affectedActivities." }
}
