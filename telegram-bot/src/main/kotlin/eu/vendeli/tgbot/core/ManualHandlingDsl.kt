package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.TelegramUpdateHandler.Companion.logger
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ActionContext
import eu.vendeli.tgbot.types.internal.CommandContext
import eu.vendeli.tgbot.types.internal.CommandSelector
import eu.vendeli.tgbot.types.internal.InputBreakPoint
import eu.vendeli.tgbot.types.internal.InputContext
import eu.vendeli.tgbot.types.internal.ManualActions
import eu.vendeli.tgbot.types.internal.SingleInputChain
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.OnCallbackQueryAction
import eu.vendeli.tgbot.utils.OnChannelPostAction
import eu.vendeli.tgbot.utils.OnChatJoinRequestAction
import eu.vendeli.tgbot.utils.OnChatMemberAction
import eu.vendeli.tgbot.utils.OnChosenInlineResultAction
import eu.vendeli.tgbot.utils.OnCommandAction
import eu.vendeli.tgbot.utils.OnEditedChannelPostAction
import eu.vendeli.tgbot.utils.OnEditedMessageAction
import eu.vendeli.tgbot.utils.OnInlineQueryAction
import eu.vendeli.tgbot.utils.OnInputAction
import eu.vendeli.tgbot.utils.OnMessageAction
import eu.vendeli.tgbot.utils.OnPollAction
import eu.vendeli.tgbot.utils.OnPollAnswerAction
import eu.vendeli.tgbot.utils.OnPreCheckoutQueryAction
import eu.vendeli.tgbot.utils.OnShippingQueryAction
import eu.vendeli.tgbot.utils.WhenNotHandledAction
import eu.vendeli.tgbot.utils.checkIsLimited
import eu.vendeli.tgbot.utils.parseCommand

/**
 * DSL for manual update management.
 *
 * @property inputListener
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions")
class ManualHandlingDsl internal constructor(
    private val bot: TelegramBot,
    private val inputListener: InputListener,
) {
    private val manualActions = ManualActions()

    /**
     * Action that is performed on the presence of Message in the Update.
     */
    fun onMessage(block: OnMessageAction) {
        manualActions.onMessage = block
    }

    /**
     * Action that is performed on the presence of EditedMessage in the Update.
     */
    fun onEditedMessage(block: OnEditedMessageAction) {
        manualActions.onEditedMessage = block
    }

    /**
     * Action that is performed on the presence of PollAnswer in the Update.
     */
    fun onPollAnswer(block: OnPollAnswerAction) {
        manualActions.onPollAnswer = block
    }

    /**
     * Action that is performed on the presence of CallbackQuery in the Update.
     */
    fun onCallbackQuery(block: OnCallbackQueryAction) {
        manualActions.onCallbackQuery = block
    }

    /**
     * Action that is performed on the presence of Poll in the Update.
     */
    fun onPoll(block: OnPollAction) {
        manualActions.onPoll = block
    }

    /**
     * Action that is performed on the presence of ChatJoinRequest in the Update.
     */
    fun onChatJoinRequest(block: OnChatJoinRequestAction) {
        manualActions.onChatJoinRequest = block
    }

    /**
     * Action that is performed on the presence of ChatMember in the Update.
     */
    fun onChatMember(block: OnChatMemberAction) {
        manualActions.onChatMember = block
    }

    /**
     * Action that is performed on the presence of MyChatMember in the Update.
     */
    fun onMyChatMember(block: OnChatMemberAction) {
        manualActions.onMyChatMember = block
    }

    /**
     * Action that is performed on the presence of ChannelPost in the Update.
     */
    fun onChannelPost(block: OnChannelPostAction) {
        manualActions.onChannelPost = block
    }

    /**
     * Action that is performed on the presence of EditedChannelPost in the Update.
     */
    fun onEditedChannelPost(block: OnEditedChannelPostAction) {
        manualActions.onEditedChannelPost = block
    }

    /**
     * Action that is performed on the presence of ChosenInlineResult in the Update.
     */
    fun onChosenInlineResult(block: OnChosenInlineResultAction) {
        manualActions.onChosenInlineResult = block
    }

    /**
     * Action that is performed on the presence of InlineQuery in the Update.
     */
    fun onInlineQuery(block: OnInlineQueryAction) {
        manualActions.onInlineQuery = block
    }

    /**
     * Action that is performed on the presence of PreCheckoutQuery in the Update.
     */
    fun onPreCheckoutQuery(block: OnPreCheckoutQueryAction) {
        manualActions.onPreCheckoutQuery = block
    }

    /**
     * Action that is performed on the presence of ShippingQuery in the Update.
     */
    fun onShippingQuery(block: OnShippingQueryAction) {
        manualActions.onShippingQuery = block
    }

    /**
     * The action that is performed when the command is matched.
     *
     * @param command The command that will be processed.
     * @param rateLimits Restriction of command requests.
     * @param block Action that will be applied.
     */
    fun onCommand(
        command: String,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        block: OnCommandAction,
    ) {
        manualActions.commands[CommandSelector.String(command, rateLimits)] = block
    }

    /**
     * The action that is performed when the command is matched.
     *
     * @param command The command that will be processed.
     * @param rateLimits Restriction of command requests.
     * @param block Action that will be applied.
     */
    fun onCommand(
        command: Regex,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        block: OnCommandAction,
    ) {
        manualActions.commands[CommandSelector.Regex(command, rateLimits)] = block
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
        block: OnInputAction,
    ) {
        manualActions.onInput[identifier] = SingleInputChain(identifier, block, rateLimits)
    }

    /**
     * Action that will be applied when none of the other handlers process the data
     */
    fun whenNotHandled(block: WhenNotHandledAction) {
        manualActions.whenNotHandled = block
    }

    /**
     * Dsl for creating chain of input processing
     *
     * @param identifier id of input
     * @param rateLimits Restriction of input requests.
     * @param block action that will be applied if input will match
     * @return [SingleInputChain] for further chaining
     */
    fun inputChain(
        identifier: String,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        block: OnInputAction,
    ): SingleInputChain {
        val firstChain = SingleInputChain(identifier, block, rateLimits)
        manualActions.onInput[identifier] = firstChain

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
        block: OnInputAction,
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

        manualActions.onInput[id]?.tail = newId
        manualActions.onInput[newId] = SingleInputChain(newId, block, rateLimits, nextLevel)
        return this
    }

    /**
     * Condition, which will cause the chain to be interrupted if it matches.
     *
     */
    fun SingleInputChain.breakIf(
        condition: InputContext.() -> Boolean,
        repeat: Boolean = true,
        block: OnInputAction? = null,
    ): SingleInputChain {
        manualActions.onInput[id]?.breakPoint = InputBreakPoint(condition, block, repeat)
        return this
    }

    /**
     * Method that tries to find action in given text and invoke action matches it
     *
     * @param update
     * @param from
     * @param text
     */
    private suspend fun checkMessageForActions(update: Update, from: User?, text: String?): Boolean {
        // parse text to chosen format
        val parsedText = text?.let { bot.update.parseCommand(it) }
        logger.debug { "Parsed text - $text to $parsedText" }

        // if there's no action then break
        if (parsedText == null || from == null) return false

        // find action which match command and invoke it
        manualActions.commands.filter { it.key.match(parsedText.command) }.entries.firstOrNull()?.run {
            logger.debug { "Matched command $this for text $text" }
            inputListener.del(from.id) // clean input listener
            // check for limit exceed
            if (bot.update.checkIsLimited(key.rateLimits, update.message?.from?.id, parsedText.command)) return false
            logger.info { "Invoking command $key" }
            value.invoke(CommandContext(update, parsedText.params, from))
            return true
        }
        // if there's no command -> then try process input
        inputListener.getAsync(from.id).await()?.also {
            logger.info { "Found inputListener point $it for ${from.id}" }
            inputListener.del(from.id) // clean listener after input caught
            // search matching input handler for listening point
            val foundChain = manualActions.onInput[it]
            if (foundChain != null && update.message != null) {
                // check for limit exceed
                if (bot.update.checkIsLimited(foundChain.rateLimits, update.message.from?.id, foundChain.id)) {
                    return false
                }
                val inputContext = InputContext(from, update)
                // invoke it if found
                // todo check prev chain br cond
                foundChain.inputAction.invoke(inputContext)
                // if there's chaining point and breaking condition wasn't match then set new listener
                val breakCondition = foundChain.breakPoint?.condition?.invoke(inputContext) == true
                if (foundChain.tail != null && !breakCondition) {
                    foundChain.breakPoint?.action?.invoke(inputContext)
                    inputListener.set(from.id, foundChain.tail!!)
                    return true
                } else if (breakCondition && foundChain.breakPoint?.repeat == true) {
                    inputListener.set(from.id, foundChain.id)
                }
            }
        }
        return false
    }

    private suspend fun <CTX> ((suspend (ActionContext<CTX>) -> Unit)?).invokeAction(
        actionType: String,
        actionCtx: ActionContext<CTX>,
    ): Boolean {
        this?.runCatching { invoke(actionCtx) }?.onFailure {
            bot.update.caughtExceptions.send(it to actionCtx.update)
            logger.error(it) {
                "An error occurred while manually processing update: ${actionCtx.update} to $actionType."
            }
        }?.onSuccess {
            logger.info { "Update ${actionCtx.update.updateId} processed in manual mode with $actionType action." }
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
    internal suspend fun process(update: Update) = with(update) {
        logger.info { "Handling update #${update.updateId}" }
        if (bot.update.checkIsLimited(bot.config.rateLimits, update.message?.from?.id)) return@with
        var affectedActions = 0

        when {
            message != null -> {
                manualActions.onMessage?.invokeAction("onMessage", ActionContext(update, message)).ifAffected {
                    affectedActions += 1
                }
                checkMessageForActions(update, update.message?.from, update.message?.text).ifAffected {
                    affectedActions += 1
                }
            }

            editedMessage != null -> manualActions.onEditedMessage?.invokeAction(
                "onEditedMessage",
                ActionContext(update, editedMessage),
            ).ifAffected {
                affectedActions += 1
            }

            pollAnswer != null -> manualActions.onPollAnswer?.invokeAction(
                "onPollAnswer",
                ActionContext(update, pollAnswer),
            ).ifAffected {
                affectedActions += 1
            }

            callbackQuery != null -> {
                manualActions.onCallbackQuery?.invokeAction(
                    "onCallbackQuery",
                    ActionContext(update, callbackQuery),
                ).ifAffected {
                    affectedActions += 1
                }
                if (callbackQuery.data == null) return@with

                checkMessageForActions(update, callbackQuery.from, callbackQuery.data).ifAffected {
                    affectedActions += 1
                }
            }

            poll != null -> manualActions.onPoll?.invokeAction(
                "onPoll",
                ActionContext(update, poll),
            ).ifAffected {
                affectedActions += 1
            }

            chatJoinRequest != null -> manualActions.onChatJoinRequest?.invokeAction(
                "onChatJoinRequest",
                ActionContext(update, chatJoinRequest),
            ).ifAffected {
                affectedActions += 1
            }

            chatMember != null -> manualActions.onChatMember?.invokeAction(
                "onChatMember",
                ActionContext(update, chatMember),
            ).ifAffected {
                affectedActions += 1
            }

            myChatMember != null -> manualActions.onMyChatMember?.invokeAction(
                "onMyChatMember",
                ActionContext(update, myChatMember),
            ).ifAffected {
                affectedActions += 1
            }

            channelPost != null -> manualActions.onChannelPost?.invokeAction(
                "onChannelPost",
                ActionContext(update, channelPost),
            ).ifAffected {
                affectedActions += 1
            }

            inlineQuery != null -> manualActions.onInlineQuery?.invokeAction(
                "onInlineQuery",
                ActionContext(update, inlineQuery),
            ).ifAffected {
                affectedActions += 1
            }

            shippingQuery != null -> manualActions.onShippingQuery?.invokeAction(
                "onShippingQuery",
                ActionContext(update, shippingQuery),
            ).ifAffected {
                affectedActions += 1
            }

            preCheckoutQuery != null -> manualActions.onPreCheckoutQuery?.invokeAction(
                "onPreCheckoutQuery",
                ActionContext(
                    update,
                    preCheckoutQuery,
                ),
            ).ifAffected {
                affectedActions += 1
            }

            editedChannelPost != null -> manualActions.onEditedChannelPost?.invokeAction(
                "onEditedChannelPost",
                ActionContext(
                    update,
                    editedChannelPost,
                ),
            ).ifAffected {
                affectedActions += 1
            }

            chosenInlineResult != null -> manualActions.onChosenInlineResult?.invokeAction(
                "onChosenInlineResult",
                ActionContext(
                    update,
                    chosenInlineResult,
                ),
            ).ifAffected {
                affectedActions += 1
            }
        }
        if (affectedActions == 0) manualActions.whenNotHandled?.invoke(update)

        logger.info { "Number of affected manual actions - $affectedActions." }
    }
}
