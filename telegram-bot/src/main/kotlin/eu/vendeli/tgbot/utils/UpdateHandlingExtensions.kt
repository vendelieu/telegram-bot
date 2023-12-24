package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.ManualHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler.Companion.logger
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ActionContext
import eu.vendeli.tgbot.types.internal.CommandContext
import eu.vendeli.tgbot.types.internal.InputContext
import eu.vendeli.tgbot.types.internal.SingleInputChain
import eu.vendeli.tgbot.types.internal.UpdateType

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
 * @param from
 * @param text
 */
@Suppress("CyclomaticComplexMethod", "DuplicatedCode", "ReturnCount")
private suspend fun ManualHandlingDsl.checkMessageForActions(
    update: Update,
    from: User?,
    text: String?,
    scope: UpdateType,
): Boolean {
    // parse text to chosen format
    val parsedText = text?.let { bot.update.parseCommand(it) }
    logger.debug { "Parsed text - $text to $parsedText" }

    // if there's no user then break
    if (from == null) return false

    // find action which match command and invoke it
    manualActions.commands[parsedText?.command]?.run {
        if (scope !in this.scope) return@run
        logger.debug { "Matched command $this for text $text" }
        bot.inputListener.del(from.id) // clean input listener
        // check for limit exceed
        if (bot.update.checkIsLimited(rateLimits, update.message?.from?.id, parsedText!!.command)) return false
        logger.info { "Invoking command $id" }
        invocation.invoke(CommandContext(update, parsedText.params, from))
        return true
    }
    // if there's no command -> then try process input
    bot.inputListener.get(from.id)?.also {
        logger.info { "Found inputListener point $it for ${from.id}" }
        bot.inputListener.del(from.id) // clean listener after input caught
        // search matching input handler for listening point
        val foundChain = manualActions.onInput[it]
        if (foundChain != null && update.message != null) {
            // check for limit exceed
            if (bot.update.checkIsLimited(foundChain.rateLimits, update.message.from?.id, foundChain.id)) {
                return false
            }
            val inputContext = InputContext(from, update) // form context for future usage
            val prevChain = foundChain.prevChainId?.let { id -> manualActions.onInput[id] }
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
                bot.inputListener.set(from.id, foundChain.id)
                return true
            }

            if (!isBreakCase && foundChain.tail != null)
                bot.inputListener.set(from.id, foundChain.tail!!)

            return true
        }
    }

    manualActions.regexCommands.entries.firstOrNull {
        it.key.matchEntire(parsedText?.command ?: return false) != null
    }?.value?.run {
        if (scope !in this.scope) return false
        logger.debug { "Matched regex command $this for text $text" }
        bot.inputListener.del(from.id) // clean input listener
        // check for limit exceed
        if (bot.update.checkIsLimited(rateLimits, update.message?.from?.id, parsedText!!.command)) return false
        logger.info { "Invoking command $id" }
        invocation.invoke(CommandContext(update, parsedText.params, from))
        return true
    }
    return false
}

private suspend fun <CTX> ((suspend (ActionContext<CTX>) -> Unit)?).invokeAction(
    bot: TelegramBot,
    actionType: String,
    actionCtx: ActionContext<CTX>,
): Boolean {
    this?.runCatching { invoke(actionCtx) }?.onFailure {
        bot.update.caughtExceptions.send(it to actionCtx.update)
        logger.error(it) {
            "An error occurred while manually processing update: ${actionCtx.update} to $actionType."
        }
    }?.onSuccess {
        logger.info { "Update #${actionCtx.update.updateId} processed in manual mode with $actionType action." }
        return true
    }
    return false
}

private inline fun Boolean?.ifAffected(block: () -> Unit) {
    if (this == true) block()
}

/**
 * Process update by manual defined actions.
 *
 * @param update
 */
@Suppress("CyclomaticComplexMethod", "LongMethod")
internal suspend fun ManualHandlingDsl.process(update: Update) = with(update) {
    logger.info { "Handling update #${update.updateId}" }
    if (bot.update.checkIsLimited(bot.config.rateLimiter.limits, update.message?.from?.id)) return@with
    var affectedActions = 0

    when {
        message != null -> {
            manualActions.onMessage?.invokeAction(bot, "onMessage", ActionContext(update, message)).ifAffected {
                affectedActions += 1
            }
            checkMessageForActions(
                update,
                update.message?.from,
                update.message?.text,
                UpdateType.MESSAGE,
            ).ifAffected {
                affectedActions += 1
            }
        }

        editedMessage != null -> manualActions.onEditedMessage?.invokeAction(
            bot,
            "onEditedMessage",
            ActionContext(update, editedMessage),
        ).ifAffected {
            affectedActions += 1
        }

        pollAnswer != null -> manualActions.onPollAnswer?.invokeAction(
            bot,
            "onPollAnswer",
            ActionContext(update, pollAnswer),
        ).ifAffected {
            affectedActions += 1
        }

        callbackQuery != null -> {
            manualActions.onCallbackQuery?.invokeAction(
                bot,
                "onCallbackQuery",
                ActionContext(update, callbackQuery),
            ).ifAffected {
                affectedActions += 1
            }
            if (callbackQuery.data == null) return@with

            checkMessageForActions(
                update,
                callbackQuery.from,
                callbackQuery.data,
                UpdateType.CALLBACK_QUERY,
            ).ifAffected {
                affectedActions += 1
            }
        }

        poll != null -> manualActions.onPoll?.invokeAction(
            bot,
            "onPoll",
            ActionContext(update, poll),
        ).ifAffected {
            affectedActions += 1
        }

        chatJoinRequest != null -> manualActions.onChatJoinRequest?.invokeAction(
            bot,
            "onChatJoinRequest",
            ActionContext(update, chatJoinRequest),
        ).ifAffected {
            affectedActions += 1
        }

        chatMember != null -> manualActions.onChatMember?.invokeAction(
            bot,
            "onChatMember",
            ActionContext(update, chatMember),
        ).ifAffected {
            affectedActions += 1
        }

        myChatMember != null -> manualActions.onMyChatMember?.invokeAction(
            bot,
            "onMyChatMember",
            ActionContext(update, myChatMember),
        ).ifAffected {
            affectedActions += 1
        }

        channelPost != null -> manualActions.onChannelPost?.invokeAction(
            bot,
            "onChannelPost",
            ActionContext(update, channelPost),
        ).ifAffected {
            affectedActions += 1
        }

        inlineQuery != null -> manualActions.onInlineQuery?.invokeAction(
            bot,
            "onInlineQuery",
            ActionContext(update, inlineQuery),
        ).ifAffected {
            affectedActions += 1
        }

        shippingQuery != null -> manualActions.onShippingQuery?.invokeAction(
            bot,
            "onShippingQuery",
            ActionContext(update, shippingQuery),
        ).ifAffected {
            affectedActions += 1
        }

        preCheckoutQuery != null -> manualActions.onPreCheckoutQuery?.invokeAction(
            bot,
            "onPreCheckoutQuery",
            ActionContext(
                update,
                preCheckoutQuery,
            ),
        ).ifAffected {
            affectedActions += 1
        }

        editedChannelPost != null -> manualActions.onEditedChannelPost?.invokeAction(
            bot,
            "onEditedChannelPost",
            ActionContext(
                update,
                editedChannelPost,
            ),
        ).ifAffected {
            affectedActions += 1
        }

        chosenInlineResult != null -> manualActions.onChosenInlineResult?.invokeAction(
            bot,
            "onChosenInlineResult",
            ActionContext(
                update,
                chosenInlineResult,
            ),
        ).ifAffected {
            affectedActions += 1
        }
    }
    if (affectedActions == 0) manualActions.whenNotHandled?.invoke(update)?.also {
        logger.info { "Update #${update.updateId} processed in manual mode with whenNotHandled action." }
        affectedActions += 1
    }

    logger.info { "Number of affected manual actions - $affectedActions." }
}
