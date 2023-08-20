package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.types.internal.CommandScope
import eu.vendeli.tgbot.types.internal.InputBreakPoint
import eu.vendeli.tgbot.types.internal.InputContext
import eu.vendeli.tgbot.types.internal.ManualActions
import eu.vendeli.tgbot.types.internal.ManualInvocation
import eu.vendeli.tgbot.types.internal.SingleInputChain
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.DEFAULT_COMMAND_SCOPE
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

/**
 * DSL for manual update management.
 *
 * @property inputListener
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions")
class ManualHandlingDsl internal constructor(
    internal val bot: TelegramBot,
    internal val inputListener: InputListener,
) {
    internal val manualActions = ManualActions()

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
        scope: Set<CommandScope> = DEFAULT_COMMAND_SCOPE,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        block: OnCommandAction,
    ) {
        manualActions.commands[command] = ManualInvocation(command, block, scope, rateLimits)
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
        scope: Set<CommandScope> = DEFAULT_COMMAND_SCOPE,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        block: OnCommandAction,
    ) {
        manualActions.regexCommands[command] = ManualInvocation(command.pattern, block, scope, rateLimits)
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
}
