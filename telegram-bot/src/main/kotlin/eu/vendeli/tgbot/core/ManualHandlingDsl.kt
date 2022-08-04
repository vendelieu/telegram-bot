package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.core.ManualHandlingDsl.ArgsMode.Query
import eu.vendeli.tgbot.core.ManualHandlingDsl.ArgsMode.SpaceKeyValue
import eu.vendeli.tgbot.interfaces.BotInputListener
import eu.vendeli.tgbot.types.*
import eu.vendeli.tgbot.types.internal.*
import eu.vendeli.tgbot.utils.parseKeyValueBySpace
import eu.vendeli.tgbot.utils.parseQuery

/**
 * DSL for manual update management.
 *
 * @property inputListener
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class ManualHandlingDsl internal constructor(
    private val inputListener: BotInputListener,
) {
    private val manualActions = ManualActions()

    /**
     * Argument parsing mode
     * @property Query command?key=value&another=value
     * @property SpaceKeyValue command key value another value
     * (note that if the key-value pair is not fulfilled, the value will be empty string)
     */
    enum class ArgsMode {
        Query, SpaceKeyValue
    }

    /**
     * Arguments parsing mode
     */
    var argsParsingMode: ArgsMode = Query

    /**
     * Action that is performed on the presence of Message in the Update.
     */
    fun onMessage(block: suspend ActionContext<Message>.() -> Unit) {
        manualActions.onMessage = block
    }

    /**
     * Action that is performed on the presence of EditedMessage in the Update.
     */
    fun onEditedMessage(block: suspend ActionContext<Message>.() -> Unit) {
        manualActions.onEditedMessage = block
    }

    /**
     * Action that is performed on the presence of PollAnswer in the Update.
     */
    fun onPollAnswer(block: suspend ActionContext<PollAnswer>.() -> Unit) {
        manualActions.onPollAnswer = block
    }

    /**
     * Action that is performed on the presence of CallbackQuery in the Update.
     */
    fun onCallbackQuery(block: suspend ActionContext<CallbackQuery>.() -> Unit) {
        manualActions.onCallbackQuery = block
    }

    /**
     * Action that is performed on the presence of Poll in the Update.
     */
    fun onPoll(block: suspend ActionContext<Poll>.() -> Unit) {
        manualActions.onPoll = block
    }

    /**
     * Action that is performed on the presence of ChatJoinRequest in the Update.
     */
    fun onChatJoinRequest(block: suspend ActionContext<ChatJoinRequest>.() -> Unit) {
        manualActions.onChatJoinRequest = block
    }

    /**
     * Action that is performed on the presence of ChatMember in the Update.
     */
    fun onChatMember(block: suspend ActionContext<ChatMemberUpdated>.() -> Unit) {
        manualActions.onChatMember = block
    }

    /**
     * Action that is performed on the presence of MyChatMember in the Update.
     */
    fun onMyChatMember(block: suspend ActionContext<ChatMemberUpdated>.() -> Unit) {
        manualActions.onMyChatMember = block
    }

    /**
     * Action that is performed on the presence of ChannelPost in the Update.
     */
    fun onChannelPost(block: suspend ActionContext<Message>.() -> Unit) {
        manualActions.onChannelPost = block
    }

    /**
     * Action that is performed on the presence of EditedChannelPost in the Update.
     */
    fun onEditedChannelPost(block: suspend ActionContext<Message>.() -> Unit) {
        manualActions.onEditedChannelPost = block
    }

    /**
     * Action that is performed on the presence of ChosenInlineResult in the Update.
     */
    fun onChosenInlineResult(block: suspend ActionContext<ChosenInlineResult>.() -> Unit) {
        manualActions.onChosenInlineResult = block
    }

    /**
     * Action that is performed on the presence of InlineQuery in the Update.
     */
    fun onInlineQuery(block: suspend ActionContext<InlineQuery>.() -> Unit) {
        manualActions.onInlineQuery = block
    }

    /**
     * Action that is performed on the presence of PreCheckoutQuery in the Update.
     */
    fun onPreCheckoutQuery(block: suspend ActionContext<PreCheckoutQuery>.() -> Unit) {
        manualActions.onPreCheckoutQuery = block
    }

    /**
     * Action that is performed on the presence of ShippingQuery in the Update.
     */
    fun onShippingQuery(block: suspend ActionContext<ShippingQuery>.() -> Unit) {
        manualActions.onShippingQuery = block
    }

    /**
     * The action that is performed when the command is matched.
     */
    fun onCommand(command: String, block: suspend CommandContext.() -> Unit) {
        manualActions.commands[CommandSelector.String(command)] = block
    }

    /**
     * The action that is performed when the command is matched.
     */
    fun onCommand(command: Regex, block: suspend CommandContext.() -> Unit) {
        manualActions.commands[CommandSelector.Regex(command)] = block
    }

    /**
     * The action that is performed when the input is matched.
     */
    fun onInput(identifier: String, block: suspend InputContext.() -> Unit) {
        manualActions.onInput[identifier] = SingleInputChain(identifier, block)
    }

    /**
     * Action that will be applied when none of the other handlers process the data
     */
    fun whenNotHandled(block: suspend Update.() -> Unit) {
        manualActions.whenNotHandled = block
    }

    /**
     * Dsl for creating chain of input processing
     *
     * @param identifier id of input
     * @param block action that will be applied if input will match
     * @return [SingleInputChain] for further chaining
     */
    fun inputChain(identifier: String, block: suspend InputContext.() -> Unit): SingleInputChain {
        val firstChain = SingleInputChain(identifier, block)
        manualActions.onInput[identifier] = firstChain

        return firstChain
    }

    /**
     * Adding a chain to the input data processing
     *
     * @param block action that will be applied if the inputs match the current chain level
     * @return [SingleInputChain] for further chaining
     */
    fun SingleInputChain.andThen(block: suspend InputContext.() -> Unit): SingleInputChain {
        val nextLevel = this.currentLevel + 1
        val newId = if (this.currentLevel > 0) this.id.replace(
            "_chain_lvl_${this.currentLevel}", "_chain_lvl_$nextLevel"
        ) else this.id + "_chain_lvl_1"

        manualActions.onInput[this.id]?.tail = newId
        manualActions.onInput[newId] = SingleInputChain(newId, block, nextLevel)
        return this
    }

    /**
     * Condition, which will cause the chain to be interrupted if it matches.
     *
     */
    fun SingleInputChain.breakIf(
        condition: InputContext.() -> Boolean,
        block: (suspend InputContext.() -> Unit)? = null,
    ): SingleInputChain {
        manualActions.onInput[this.id]?.breakPoint = InputBreakPoint(condition, block)
        return this
    }

    /**
     * Process update by manual defined actions.
     *
     * @param update
     */
    suspend fun process(update: Update) = with(update) {
        when {
            message != null -> {
                // invoke 'on-message' action
                manualActions.onMessage?.invoke(ActionContext(update, message))
                // find command by chosen format from text
                val structuredRequest = if (argsParsingMode == Query) message.text?.parseQuery()
                else message.text?.parseKeyValueBySpace()

                // process command if there's one
                structuredRequest?.also { r ->
                    // find action which match command
                    val action = manualActions.commands.filter { it.key.match(r.command) }.values.firstOrNull()
                    // invoke if found
                    action?.invoke(CommandContext(update, r.params, update.message?.from!!))

                    // if there's no command > then try process input
                    if (action == null) {
                        inputListener.getAsync(message.from!!.id).await()?.also {
                            inputListener.del(message.from.id) // clean listener after input caught
                            // search matching input handler for listening point
                            val foundChain = manualActions.onInput[it]
                            if (foundChain != null && update.message != null && update.message?.from != null) {
                                val inputContext = InputContext(update.message.from, update.message)
                                // invoke it if found
                                foundChain.inputAction.invoke(inputContext)
                                // if there's chaining point and breaking condition wasn't match then set new listener
                                if (foundChain.tail != null && foundChain.breakPoint?.condition?.invoke(inputContext) == false) {
                                    foundChain.breakPoint?.action?.invoke(inputContext)
                                    inputListener.set(message.from.id, foundChain.tail!!)
                                }
                            }
                        }
                    } else inputListener.del(message.from!!.id) // if action for command nevertheless was found > clean listener
                }
            }

            editedMessage != null -> manualActions.onEditedMessage?.invoke(ActionContext(update, editedMessage))
            pollAnswer != null -> manualActions.onPollAnswer?.invoke(ActionContext(update, pollAnswer))
            callbackQuery != null -> manualActions.onCallbackQuery?.invoke(ActionContext(update, callbackQuery))
            poll != null -> manualActions.onPoll?.invoke(ActionContext(update, poll))
            chatJoinRequest != null -> manualActions.onChatJoinRequest?.invoke(ActionContext(update, chatJoinRequest))
            chatMember != null -> manualActions.onChatMember?.invoke(ActionContext(update, chatMember))
            myChatMember != null -> manualActions.onMyChatMember?.invoke(ActionContext(update, myChatMember))
            channelPost != null -> manualActions.onChannelPost?.invoke(ActionContext(update, channelPost))
            inlineQuery != null -> manualActions.onInlineQuery?.invoke(ActionContext(update, inlineQuery))
            shippingQuery != null -> manualActions.onShippingQuery?.invoke(ActionContext(update, shippingQuery))
            preCheckoutQuery != null -> manualActions.onPreCheckoutQuery?.invoke(
                ActionContext(
                    update, preCheckoutQuery
                )
            )

            editedChannelPost != null -> manualActions.onEditedChannelPost?.invoke(
                ActionContext(
                    update, editedChannelPost
                )
            )

            chosenInlineResult != null -> manualActions.onChosenInlineResult?.invoke(
                ActionContext(
                    update, chosenInlineResult
                )
            )

            else -> manualActions.whenNotHandled?.invoke(update)
        }
        Unit
    }
}
