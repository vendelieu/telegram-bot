package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler.Companion.logger
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.ActivityCtx
import eu.vendeli.tgbot.types.internal.CommandContext
import eu.vendeli.tgbot.types.internal.FailedUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.SingleInputChain
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.userOrNull

private inline val SingleInputChain.prevChainId: String?
    get() = if (currentLevel == 1) {
        id.replace("_chain_lvl_$currentLevel", "")
    } else if (currentLevel > 1) {
        id.replace("_chain_lvl_$currentLevel", "_chain_lvl_${currentLevel - 1}")
    } else {
        null
    }

/**
 * Method that tries to find action in given text and invoke action matches it
 *
 * @param update
 */
@Suppress("CyclomaticComplexMethod", "NestedBlockDepth", "ReturnCount")
private suspend fun FunctionalHandlingDsl.checkMessageForActivities(update: ProcessedUpdate): Boolean = update.run {
    // parse text to chosen format
    val parsedText = text.let { bot.update.parseCommand(it) }
    logger.debug { "Parsed text - $text to $parsedText" }
    val user = userOrNull
    val cmdCtx = CommandContext(update, parsedText.params)

    // find action which match command and invoke it
    functionalActivities.commands[parsedText.command to type]?.run {
        logger.debug { "Matched command ${parsedText.command} for text $text" }
        if (user != null) bot.inputListener.del(user.id) // clean input listener
        // check for limit exceed
        if (bot.update.checkIsLimited(rateLimits, user?.id, parsedText.command)) return false
        logger.info { "Invoking command $id" }
        invocation.invoke(cmdCtx)
        return true
    }
    // if there's no command -> then try process input
    if (user != null) bot.inputListener.get(user.id)?.also {
        logger.info { "Found inputListener point $it for ${user.id}" }

        // search matching input handler for listening point
        val foundChain = functionalActivities.inputs[it]
        if (foundChain != null) {
            // check for limit exceed
            if (bot.update.checkIsLimited(foundChain.rateLimits, user.id, foundChain.id)) return false

            val inputContext = ActivityCtx(this) // form context for future usage
            val prevChain = foundChain.prevChainId?.let { id -> functionalActivities.inputs[id] }
            // check is there prev chain to check break condition for current lvl
            val isBreakCase = prevChain?.breakPoint?.condition?.invoke(inputContext) == true

            if (!isBreakCase) {
                foundChain.inputAction.invoke(inputContext)
                // invoke chain if break condition is false
            } else {
                prevChain?.breakPoint?.action?.invoke(inputContext)
                // invoke break point action when it's break case
            }

            if (isBreakCase && prevChain?.breakPoint?.repeat == true) {
                // and if we need to repeat do set listener again
                bot.inputListener.set(user.id, foundChain.id)
                return true
            }

            if (!isBreakCase && foundChain.tail != null)
                bot.inputListener.set(user.id, foundChain.tail!!)

            return true
        }
    }
    if (user != null && bot.config.inputAutoRemoval) bot.inputListener.del(user.id) // clean listener

    if (parsedText.command.isNotBlank()) functionalActivities.regexCommands.entries.firstOrNull { i ->
        i.key.matchEntire(parsedText.command) != null
    }?.value?.run {
        logger.debug { "Matched regex command $this for text $text" }
        // check for limit exceed
        if (bot.update.checkIsLimited(rateLimits, user?.id, parsedText.command)) return false
        logger.info { "Invoking command $id" }
        invocation.invoke(cmdCtx)
        return true
    }
    return false
}

private suspend fun ((suspend ActivityCtx<ProcessedUpdate>.() -> Unit)?).invokeAction(
    bot: TelegramBot,
    updateType: UpdateType,
    actionCtx: ActivityCtx<ProcessedUpdate>,
): Boolean {
    this?.runCatching { invoke(actionCtx) }?.onFailure {
        bot.update.caughtExceptions.send(FailedUpdate(it.cause ?: it, actionCtx.update.update))
        logger.error(it) {
            "An error occurred while functionally processing update: ${actionCtx.update} to UpdateType($updateType)."
        }
    }?.onSuccess {
        logger.info {
            "Update #${actionCtx.update.updateId} processed in functional mode with UpdateType($updateType) action."
        }
        return true
    }
    return false
}

private inline fun Boolean?.ifAffected(block: () -> Unit) {
    if (this == true) block()
}

/**
 * Process update by functional defined actions.
 *
 * @param update
 */
@Suppress("CyclomaticComplexMethod", "LongMethod")
internal suspend fun FunctionalHandlingDsl.process(update: Update) = with(update.processUpdate()) {
    logger.info { "Handling update #${update.updateId}" }
    if (bot.update.checkIsLimited(bot.config.rateLimiter.limits, userOrNull?.id)) return@with
    var affectedActions = 0

    checkMessageForActivities(this).ifAffected { affectedActions += 1 }
    functionalActivities.onUpdateActivities[type]?.invokeAction(bot, type, ActivityCtx(this))
        .ifAffected { affectedActions += 1 }

    if (affectedActions == 0) functionalActivities.whenNotHandled?.invoke(update)?.also {
        logger.info { "Update #${update.updateId} processed in functional mode with whenNotHandled action." }
        affectedActions += 1
    }

    logger.info { "Number of affected functional actions - $affectedActions." }
}
