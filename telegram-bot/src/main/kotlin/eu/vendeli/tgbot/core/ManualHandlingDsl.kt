package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.core.ManualHandlingDsl.ArgsMode.Query
import eu.vendeli.tgbot.core.ManualHandlingDsl.ArgsMode.SpaceKeyValue
import eu.vendeli.tgbot.interfaces.BotInputListener
import eu.vendeli.tgbot.types.*
import eu.vendeli.tgbot.types.internal.CommandContext
import eu.vendeli.tgbot.types.internal.CommandSelector
import eu.vendeli.tgbot.types.internal.ManualActions
import eu.vendeli.tgbot.types.internal.SingleInputChain
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
    fun onMessage(block: suspend Message.() -> Unit) {
        manualActions.onMessage = block
    }

    /**
     * Action that is performed on the presence of EditedMessage in the Update.
     */
    fun onEditedMessage(block: suspend Message.() -> Unit) {
        manualActions.onEditedMessage = block
    }

    /**
     * Action that is performed on the presence of PollAnswer in the Update.
     */
    fun onPollAnswer(block: suspend PollAnswer.() -> Unit) {
        manualActions.onPollAnswer = block
    }

    /**
     * Action that is performed on the presence of CallbackQuery in the Update.
     */
    fun onCallbackQuery(block: suspend CallbackQuery.() -> Unit) {
        manualActions.onCallbackQuery = block
    }

    /**
     * Action that is performed on the presence of Poll in the Update.
     */
    fun onPoll(block: suspend Poll.() -> Unit) {
        manualActions.onPoll = block
    }

    /**
     * Action that is performed on the presence of ChatJoinRequest in the Update.
     */
    fun onChatJoinRequest(block: suspend ChatJoinRequest.() -> Unit) {
        manualActions.onChatJoinRequest = block
    }

    /**
     * Action that is performed on the presence of ChatMember in the Update.
     */
    fun onChatMember(block: suspend ChatMemberUpdated.() -> Unit) {
        manualActions.onChatMember = block
    }

    /**
     * Action that is performed on the presence of MyChatMember in the Update.
     */
    fun onMyChatMember(block: suspend ChatMemberUpdated.() -> Unit) {
        manualActions.onMyChatMember = block
    }

    /**
     * Action that is performed on the presence of ChannelPost in the Update.
     */
    fun onChannelPost(block: suspend Message.() -> Unit) {
        manualActions.onChannelPost = block
    }

    /**
     * Action that is performed on the presence of EditedChannelPost in the Update.
     */
    fun onEditedChannelPost(block: suspend Message.() -> Unit) {
        manualActions.onEditedChannelPost = block
    }

    /**
     * Action that is performed on the presence of ChosenInlineResult in the Update.
     */
    fun onChosenInlineResult(block: suspend ChosenInlineResult.() -> Unit) {
        manualActions.onChosenInlineResult = block
    }

    /**
     * Action that is performed on the presence of InlineQuery in the Update.
     */
    fun onInlineQuery(block: suspend InlineQuery.() -> Unit) {
        manualActions.onInlineQuery = block
    }

    /**
     * Action that is performed on the presence of PreCheckoutQuery in the Update.
     */
    fun onPreCheckoutQuery(block: suspend PreCheckoutQuery.() -> Unit) {
        manualActions.onPreCheckoutQuery = block
    }

    /**
     * Action that is performed on the presence of ShippingQuery in the Update.
     */
    fun onShippingQuery(block: suspend ShippingQuery.() -> Unit) {
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
    fun onInput(identifier: String, block: suspend () -> Unit) {
        manualActions.onInput[identifier] = SingleInputChain(identifier, block)
    }

    /**
     * Action that will be applied when none of the other handlers process the data
     */
    fun whenNotHandled(block: suspend () -> Unit) {
        manualActions.whenNotHandled = block
    }

    /**
     * Dsl for creating chain of input processing
     *
     * @param identifier id of input
     * @param block action that will be applied if input will match
     * @return [SingleInputChain] for further chaining
     */
    fun inputChain(identifier: String, block: suspend () -> Unit): SingleInputChain {
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
    fun SingleInputChain.andThen(block: suspend () -> Unit): SingleInputChain {
        val nextLevel = this.currentLevel + 1
        val newId = if (this.currentLevel > 0) this.id.replace(
            "_chain_lvl_${this.currentLevel}",
            "_chain_lvl_$nextLevel"
        ) else this.id + "_chain_lvl_1"

        manualActions.onInput[this.id]?.tail = newId
        manualActions.onInput[newId] = SingleInputChain(newId, block, nextLevel)
        return this
    }

    /**
     * Condition, which will cause the chain to be interrupted if it matches.
     *
     */
    fun SingleInputChain.breakIf(block: () -> Boolean): SingleInputChain {
        manualActions.onInput[this.id]?.breakCondition = block
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
                manualActions.onMessage?.invoke(message)
                // find command by chosen format from text
                val structuredRequest = if (argsParsingMode == Query) message.text?.parseQuery()
                else message.text?.parseKeyValueBySpace()

                // process command if there's one
                structuredRequest?.also { r ->
                    // find action which match command
                    val action = manualActions.commands.filter { it.key.match(r.command) }.values.firstOrNull()
                    // invoke if found
                    action?.invoke(CommandContext(r.params, update.message?.from!!))

                    // if there's no command > then try process input
                    if (action == null) {
                        inputListener.getAsync(message.from!!.id).await()?.also {
                            inputListener.del(message.from.id) // clean listener after input caught
                            // search matching input handler for listening point
                            manualActions.onInput[it]?.also { chain ->
                                // invoke it if found
                                chain.inputAction.invoke()
                                // if there's chaining point and breaking condition wasn't match then set new listener
                                if (chain.tail != null && chain.breakCondition?.invoke() == false) inputListener.set(
                                    message.from.id,
                                    chain.tail!!
                                )
                            }
                        }
                    } else inputListener.del(message.from!!.id) // if action for command nevertheless was found > clean listener
                }
            }

            editedMessage != null -> manualActions.onEditedMessage?.invoke(editedMessage)
            pollAnswer != null -> manualActions.onPollAnswer?.invoke(pollAnswer)
            callbackQuery != null -> manualActions.onCallbackQuery?.invoke(callbackQuery)
            poll != null -> manualActions.onPoll?.invoke(poll)
            chatJoinRequest != null -> manualActions.onChatJoinRequest?.invoke(chatJoinRequest)
            chatMember != null -> manualActions.onChatMember?.invoke(chatMember)
            myChatMember != null -> manualActions.onMyChatMember?.invoke(myChatMember)
            channelPost != null -> manualActions.onChannelPost?.invoke(channelPost)
            editedChannelPost != null -> manualActions.onEditedChannelPost?.invoke(editedChannelPost)
            chosenInlineResult != null -> manualActions.onChosenInlineResult?.invoke(chosenInlineResult)
            inlineQuery != null -> manualActions.onInlineQuery?.invoke(inlineQuery)
            preCheckoutQuery != null -> manualActions.onPreCheckoutQuery?.invoke(preCheckoutQuery)
            shippingQuery != null -> manualActions.onShippingQuery?.invoke(shippingQuery)
            else -> manualActions.whenNotHandled?.invoke()
        }
        Unit
    }
}
