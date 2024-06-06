package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.Guard
import eu.vendeli.tgbot.types.internal.ActivityCtx
import eu.vendeli.tgbot.types.internal.FunctionalActivities
import eu.vendeli.tgbot.types.internal.FunctionalInvocation
import eu.vendeli.tgbot.types.internal.InputBreakPoint
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.SingleInputChain
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.DEFAULT_COMMAND_SCOPE
import eu.vendeli.tgbot.utils.DefaultGuard
import eu.vendeli.tgbot.utils.OnBusinessConnectionActivity
import eu.vendeli.tgbot.utils.OnBusinessMessageActivity
import eu.vendeli.tgbot.utils.OnCallbackQueryActivity
import eu.vendeli.tgbot.utils.OnChannelPostActivity
import eu.vendeli.tgbot.utils.OnChatBoostActivity
import eu.vendeli.tgbot.utils.OnChatJoinRequestActivity
import eu.vendeli.tgbot.utils.OnChatMemberActivity
import eu.vendeli.tgbot.utils.OnChosenInlineResultActivity
import eu.vendeli.tgbot.utils.OnCommandActivity
import eu.vendeli.tgbot.utils.OnDeletedBusinessMessagesActivity
import eu.vendeli.tgbot.utils.OnEditedBusinessMessageActivity
import eu.vendeli.tgbot.utils.OnEditedChannelPostActivity
import eu.vendeli.tgbot.utils.OnEditedMessageActivity
import eu.vendeli.tgbot.utils.OnInlineQueryActivity
import eu.vendeli.tgbot.utils.OnInputActivity
import eu.vendeli.tgbot.utils.OnMessageActivity
import eu.vendeli.tgbot.utils.OnMessageReactionActivity
import eu.vendeli.tgbot.utils.OnMessageReactionCountActivity
import eu.vendeli.tgbot.utils.OnMyChatMemberActivity
import eu.vendeli.tgbot.utils.OnPollActivity
import eu.vendeli.tgbot.utils.OnPollAnswerActivity
import eu.vendeli.tgbot.utils.OnPreCheckoutQueryActivity
import eu.vendeli.tgbot.utils.OnRemovedChatBoostActivity
import eu.vendeli.tgbot.utils.OnShippingQueryActivity
import eu.vendeli.tgbot.utils.WhenNotHandledActivity
import eu.vendeli.tgbot.utils.cast
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

    /**
     * Action that is performed on the presence of Message in the Update.
     */
    fun onMessage(block: OnMessageActivity) {
        functionalActivities.onUpdateActivities[UpdateType.MESSAGE] = block.cast()
    }

    /**
     * Action that is performed on the presence of EditedMessage in the Update.
     */
    fun onEditedMessage(block: OnEditedMessageActivity) {
        functionalActivities.onUpdateActivities[UpdateType.EDIT_MESSAGE] = block.cast()
    }

    /**
     * Action that is performed on the presence of PollAnswer in the Update.
     */
    fun onPollAnswer(block: OnPollAnswerActivity) {
        functionalActivities.onUpdateActivities[UpdateType.POLL_ANSWER] = block.cast()
    }

    /**
     * Action that is performed on the presence of CallbackQuery in the Update.
     */
    fun onCallbackQuery(block: OnCallbackQueryActivity) {
        functionalActivities.onUpdateActivities[UpdateType.CALLBACK_QUERY] = block.cast()
    }

    /**
     * Action that is performed on the presence of Poll in the Update.
     */
    fun onPoll(block: OnPollActivity) {
        functionalActivities.onUpdateActivities[UpdateType.POLL] = block.cast()
    }

    /**
     * Action that is performed on the presence of ChatJoinRequest in the Update.
     */
    fun onChatJoinRequest(block: OnChatJoinRequestActivity) {
        functionalActivities.onUpdateActivities[UpdateType.CHAT_JOIN_REQUEST] = block.cast()
    }

    /**
     * Action that is performed on the presence of ChatMember in the Update.
     */
    fun onChatMember(block: OnChatMemberActivity) {
        functionalActivities.onUpdateActivities[UpdateType.CHAT_MEMBER] = block.cast()
    }

    /**
     * Action that is performed on the presence of MyChatMember in the Update.
     */
    fun onMyChatMember(block: OnMyChatMemberActivity) {
        functionalActivities.onUpdateActivities[UpdateType.MY_CHAT_MEMBER] = block.cast()
    }

    /**
     * Action that is performed on the presence of ChannelPost in the Update.
     */
    fun onChannelPost(block: OnChannelPostActivity) {
        functionalActivities.onUpdateActivities[UpdateType.CHANNEL_POST] = block.cast()
    }

    /**
     * Action that is performed on the presence of EditedChannelPost in the Update.
     */
    fun onEditedChannelPost(block: OnEditedChannelPostActivity) {
        functionalActivities.onUpdateActivities[UpdateType.EDITED_CHANNEL_POST] = block.cast()
    }

    /**
     * Action that is performed on the presence of ChosenInlineResult in the Update.
     */
    fun onChosenInlineResult(block: OnChosenInlineResultActivity) {
        functionalActivities.onUpdateActivities[UpdateType.CHOSEN_INLINE_RESULT] = block.cast()
    }

    /**
     * Action that is performed on the presence of InlineQuery in the Update.
     */
    fun onInlineQuery(block: OnInlineQueryActivity) {
        functionalActivities.onUpdateActivities[UpdateType.INLINE_QUERY] = block.cast()
    }

    /**
     * Action that is performed on the presence of PreCheckoutQuery in the Update.
     */
    fun onPreCheckoutQuery(block: OnPreCheckoutQueryActivity) {
        functionalActivities.onUpdateActivities[UpdateType.PRE_CHECKOUT_QUERY] = block.cast()
    }

    /**
     * Action that is performed on the presence of ShippingQuery in the Update.
     */
    fun onShippingQuery(block: OnShippingQueryActivity) {
        functionalActivities.onUpdateActivities[UpdateType.SHIPPING_QUERY] = block.cast()
    }

    /**
     * Action that is performed on the presence of MessageReaction in the Update.
     */
    fun onMessageReaction(block: OnMessageReactionActivity) {
        functionalActivities.onUpdateActivities[UpdateType.MESSAGE_REACTION] = block.cast()
    }

    /**
     * Action that is performed on the presence of MessageReactionCount in the Update.
     */
    fun onMessageReactionCount(block: OnMessageReactionCountActivity) {
        functionalActivities.onUpdateActivities[UpdateType.MESSAGE_REACTION_COUNT] = block.cast()
    }

    /**
     * Action that is performed on the presence of ChatBoost in the Update.
     */
    fun onChatBoost(block: OnChatBoostActivity) {
        functionalActivities.onUpdateActivities[UpdateType.CHAT_BOOST] = block.cast()
    }

    /**
     * Action that is performed on the presence of RemovedChatBoost in the Update.
     */
    fun onRemovedChatBoost(block: OnRemovedChatBoostActivity) {
        functionalActivities.onUpdateActivities[UpdateType.REMOVED_CHAT_BOOST] = block.cast()
    }

    /**
     * Action that is performed on the presence of BusinessConnection in the Update.
     */
    fun onBusinessConnection(block: OnBusinessConnectionActivity) {
        functionalActivities.onUpdateActivities[UpdateType.BUSINESS_CONNECTION] = block.cast()
    }

    /**
     * Action that is performed on the presence of BusinessMessage in the Update.
     */
    fun onBusinessMessage(block: OnBusinessMessageActivity) {
        functionalActivities.onUpdateActivities[UpdateType.BUSINESS_MESSAGE] = block.cast()
    }

    /**
     * Action that is performed on the presence of EditedBusinessMessage in the Update.
     */
    fun onEditedBusinessMessage(block: OnEditedBusinessMessageActivity) {
        functionalActivities.onUpdateActivities[UpdateType.EDITED_BUSINESS_MESSAGE] = block.cast()
    }

    /**
     * Action that is performed on the presence of DeletedBusinessMessages in the Update.
     */
    fun onDeletedBusinessMessages(block: OnDeletedBusinessMessagesActivity) {
        functionalActivities.onUpdateActivities[UpdateType.DELETED_BUSINESS_MESSAGES] = block.cast()
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
        scope: Set<UpdateType> = DEFAULT_COMMAND_SCOPE,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        guard: KClass<out Guard> = DefaultGuard::class,
        block: OnCommandActivity,
    ) {
        scope.forEach {
            functionalActivities.commands[command to it] =
                FunctionalInvocation(command, block, scope, rateLimits, guard)
        }
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
        scope: Set<UpdateType> = DEFAULT_COMMAND_SCOPE,
        rateLimits: RateLimits = RateLimits.NOT_LIMITED,
        guard: KClass<out Guard> = DefaultGuard::class,
        block: OnCommandActivity,
    ) {
        functionalActivities.regexCommands[command] =
            FunctionalInvocation(command.pattern, block, scope, rateLimits, guard)
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
        guard: KClass<out Guard> = DefaultGuard::class,
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
        guard: KClass<out Guard> = DefaultGuard::class,
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
        guard: KClass<out Guard> = DefaultGuard::class,
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
