package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.core.ManualHandlingDsl.ArgsMode.Query
import eu.vendeli.tgbot.core.ManualHandlingDsl.ArgsMode.SpaceKeyValue
import eu.vendeli.tgbot.interfaces.BotInputListener
import eu.vendeli.tgbot.types.*
import eu.vendeli.tgbot.utils.parseKeyValueBySpace
import eu.vendeli.tgbot.utils.parseQuery
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * DSL for manual update management.
 *
 * @property inputListener
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class ManualHandlingDsl internal constructor(
    private val inputListener: BotInputListener,
) {
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
    suspend fun onMessage(block: suspend CoroutineContext.() -> Unit) {
        if (update.message != null) coroutineContext.block()
    }

    /**
     * Action that is performed on the presence of EditedMessage in the Update.
     */
    suspend fun onEditedMessage(block: suspend CoroutineContext.(Message) -> Unit) {
        if (update.editedMessage != null) coroutineContext.block(update.editedMessage)
    }

    /**
     * Action that is performed on the presence of PollAnswer in the Update.
     */
    suspend fun onPollAnswer(block: suspend CoroutineContext.(PollAnswer) -> Unit) {
        if (update.pollAnswer != null) coroutineContext.block(update.pollAnswer)
    }

    /**
     * Action that is performed on the presence of CallbackQuery in the Update.
     */
    suspend fun onCallbackQuery(block: suspend CoroutineContext.(CallbackQuery) -> Unit) {
        if (update.callbackQuery != null) coroutineContext.block(update.callbackQuery)
    }

    /**
     * Action that is performed on the presence of Poll in the Update.
     */
    suspend fun onPoll(block: suspend CoroutineContext.(Poll) -> Unit) {
        if (update.poll != null) coroutineContext.block(update.poll)
    }

    /**
     * Action that is performed on the presence of ChatJoinRequest in the Update.
     */
    suspend fun onChatJoinRequest(block: suspend CoroutineContext.(ChatJoinRequest) -> Unit) {
        if (update.chatJoinRequest != null) coroutineContext.block(update.chatJoinRequest)
    }

    /**
     * Action that is performed on the presence of ChatMember in the Update.
     */
    suspend fun onChatMember(block: suspend CoroutineContext.(ChatMemberUpdated) -> Unit) {
        if (update.chatMember != null) coroutineContext.block(update.chatMember)
    }

    /**
     * Action that is performed on the presence of MyChatMember in the Update.
     */
    suspend fun onMyChatMember(block: suspend CoroutineContext.(ChatMemberUpdated) -> Unit) {
        if (update.myChatMember != null) coroutineContext.block(update.myChatMember)
    }

    /**
     * Action that is performed on the presence of ChannelPost in the Update.
     */
    suspend fun onChannelPost(block: suspend CoroutineContext.(Message) -> Unit) {
        if (update.channelPost != null) coroutineContext.block(update.channelPost)
    }

    /**
     * Action that is performed on the presence of EditedChannelPost in the Update.
     */
    suspend fun onEditedChannelPost(block: suspend CoroutineContext.(Message) -> Unit) {
        if (update.editedChannelPost != null) coroutineContext.block(update.editedChannelPost)
    }

    /**
     * Action that is performed on the presence of ChosenInlineResult in the Update.
     */
    suspend fun onChosenInlineResult(block: suspend CoroutineContext.(ChosenInlineResult) -> Unit) {
        if (update.chosenInlineResult != null) coroutineContext.block(update.chosenInlineResult)
    }

    /**
     * Action that is performed on the presence of InlineQuery in the Update.
     */
    suspend fun onInlineQuery(block: suspend CoroutineContext.(InlineQuery) -> Unit) {
        if (update.inlineQuery != null) coroutineContext.block(update.inlineQuery)
    }

    /**
     * Action that is performed on the presence of PreCheckoutQuery in the Update.
     */
    suspend fun onPreCheckoutQuery(block: suspend CoroutineContext.(PreCheckoutQuery) -> Unit) {
        if (update.preCheckoutQuery != null) coroutineContext.block(update.preCheckoutQuery)
    }

    /**
     * Action that is performed on the presence of ShippingQuery in the Update.
     */
    suspend fun onShippingQuery(block: suspend CoroutineContext.(ShippingQuery) -> Unit) {
        if (update.shippingQuery != null) coroutineContext.block(update.shippingQuery)
    }

    /**
     * The action that is performed when the command is matched.
     */
    suspend fun onCommand(command: String, block: suspend CoroutineContext.(Map<String, String>) -> Unit) {
        update.message?.text?.also {
            val structuredRequest = if (argsParsingMode == Query) it.parseQuery() else it.parseKeyValueBySpace()
            if (structuredRequest.command == command) coroutineContext.block(structuredRequest.params)
        }
    }

    /**
     * The action that is performed when the command is matched.
     */
    suspend fun onCommand(command: Regex, block: suspend CoroutineContext.(Map<String, String>) -> Unit) {
        update.message?.text?.also {
            val structuredRequest = if (argsParsingMode == Query) it.parseQuery() else it.parseKeyValueBySpace()
            if (command.matches(structuredRequest.command)) coroutineContext.block(structuredRequest.params)
        }
    }

    /**
     * The action that is performed when the input is matched.
     */
    suspend fun onInput(identifier: String, block: suspend CoroutineContext.() -> Unit) {
        update.message?.from?.id?.also {
            if (waitingInput.get(it) == identifier) coroutineContext.block()
        }
    }
}
